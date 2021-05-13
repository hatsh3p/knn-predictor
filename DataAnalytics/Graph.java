import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Graph extends JPanel {

	private static final long serialVersionUID = 1L;
	private int labelPadding = 40;
	private Color lineColor = new Color(255, 255, 254);
	private Color blue = new Color(0, 0, 255); // truePositive
	private Color cyan = new Color(0, 255, 255); // falsePositive
	private Color yellow = new Color(255, 255, 0); // falseNegative
	private Color red = new Color(255, 0, 0); // trueNegative

	private Color gridColor = new Color(200, 200, 200, 200);
	private static final Stroke GRAPH_STROKE = new BasicStroke(2f);

	private static int pointWidth = 6;

	// Number of grids and the padding width
	private int numXGridLines = 6;
	private int numYGridLines = 6;
	private int padding = 40;

	private static double accuracy = -0.1;
	private static double precision = -0.1;

	private ArrayList<DataPoint> data;

	private KNNPredictor knnPredictor;

	public Graph(int K, String fileName) {
		setKAndRedraw(K, fileName);
	}
	
	public void setKAndRedraw(int K, String fileName) {
		knnPredictor = new KNNPredictor(K);
		data = knnPredictor.readData(fileName);

		accuracy = knnPredictor.getAccuracy(data) * 100;
		precision = knnPredictor.getPrecision(data) * 100;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		double minF1 = getMinF1Data();
		double maxF1 = getMaxF1Data();
		double minF2 = getMinF2Data();
		double maxF2 = getMaxF2Data();

		g2.setColor(Color.WHITE);
		g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding,
				getHeight() - 2 * padding - labelPadding);
		g2.setColor(Color.BLUE);

		double yGridRatio = (maxF2 - minF2) / numYGridLines;
		for (int i = 0; i < numYGridLines + 1; i++) {
			int x0 = padding + labelPadding;
			int x1 = pointWidth + padding + labelPadding;
			int y0 = getHeight()
					- ((i * (getHeight() - padding * 2 - labelPadding)) / numYGridLines + padding + labelPadding);
			int y1 = y0;
			if (data.size() > 0) {
				g2.setColor(gridColor);
				g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
				g2.setColor(Color.BLACK);
				String yLabel = String.format("%.2f", (minF2 + (i * yGridRatio)));
				FontMetrics metrics = g2.getFontMetrics();
				int labelWidth = metrics.stringWidth(yLabel);
				g2.drawString(yLabel, x0 - labelWidth - 6, y0 + (metrics.getHeight() / 2) - 3);
			}
			g2.drawLine(x0, y0, x1, y1);
		}

		double xGridRatio = (maxF1 - minF1) / numXGridLines;
		for (int i = 0; i < numXGridLines + 1; i++) {
			int y0 = getHeight() - padding - labelPadding;
			int y1 = y0 - pointWidth;
			int x0 = i * (getWidth() - padding * 2 - labelPadding) / (numXGridLines) + padding + labelPadding;
			int x1 = x0;
			if (data.size() > 0) {
				g2.setColor(gridColor);
				g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
				g2.setColor(Color.BLACK);
				String xLabel = String.format("%.2f", (minF1 + (i * xGridRatio)));
				FontMetrics metrics = g2.getFontMetrics();
				int labelWidth = metrics.stringWidth(xLabel);
				g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
			}
			g2.drawLine(x0, y0, x1, y1);
		}

		// Draw the main axis
		g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
		g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding,
				getHeight() - padding - labelPadding);

		// Draw the points
		paintPoints(g2, minF1, maxF1, minF2, maxF2);
	}

	private void paintPoints(Graphics2D g2, double minF1, double maxF1, double minF2, double maxF2) {
		Stroke oldStroke = g2.getStroke();
		g2.setColor(lineColor);
		g2.setStroke(GRAPH_STROKE);
		double xScale = ((double) getWidth() - (3 * padding) - labelPadding) / (maxF1 - minF1);
		double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (maxF2 - minF2);
		g2.setStroke(oldStroke);

		for (int i = 0; i < data.size(); i++) {
			int x1 = (int) ((data.get(i).getField1() - minF1) * xScale + padding + labelPadding);
			int y1 = (int) ((maxF2 - data.get(i).getField2()) * yScale + padding);
			int x = x1 - pointWidth / 2;
			int y = y1 - pointWidth / 2;
			int ovalW = pointWidth;
			int ovalH = pointWidth;

			// Skip train data
			if (!data.get(i).isATestDataPoint()) {
				continue;
			}

			double actualLabel = data.get(i).getLabel();
			double predictedLabel = Double.parseDouble(knnPredictor.test(data.get(i)));

			if (actualLabel == 1.0) {
				if (predictedLabel == 1.0) {
					g2.setColor(blue); // truePositive
				} else {
					g2.setColor(cyan); // falsePositive
				}
			} else {
				if (predictedLabel == 1.0) {
					g2.setColor(yellow); // falseNegative
				} else {
					g2.setColor(red); // trueNegative
				}
			}

			g2.fillOval(x, y, ovalW, ovalH);

		}

	}

	/*
	 * @Return the min values
	 */
	private double getMinF1Data() {
		double minData = Double.MAX_VALUE;
		for (DataPoint pt : this.data) {
			minData = Math.min(minData, pt.getField1());
		}
		return minData;
	}

	private double getMinF2Data() {
		double minData = Double.MAX_VALUE;
		for (DataPoint pt : this.data) {
			minData = Math.min(minData, pt.getField2());
		}
		return minData;
	}

	/*
	 * @Return the max values;
	 */
	private double getMaxF1Data() {
		double maxData = Double.MIN_VALUE;
		for (DataPoint pt : this.data) {
			maxData = Math.max(maxData, pt.getField1());
		}
		return maxData;
	}

	private double getMaxF2Data() {
		double maxData = Double.MIN_VALUE;
		for (DataPoint pt : this.data) {
			maxData = Math.max(maxData, pt.getField2());
		}
		return maxData;
	}

	/* Mutator */
	public void setData(ArrayList<DataPoint> data) {
		this.data = data;
		invalidate();
		this.repaint();
	}

	/* Accessor */
	public List<DataPoint> getData() {
		return data;
	}

	/*
	 * Run createAndShowGui in the main method, where we create the frame too and
	 * pack it in the panel
	 */
	
	private static void createAndShowGui(int K, String fileName) {

		/* Main panel */
		int value = K;
		Graph mainPanel = new Graph(value, fileName);

		mainPanel.setPreferredSize(new Dimension(700, 600));
		
		/* creating the frame */
		JFrame frame = new JFrame("CS 112 Lab Part 3");
		Container contentPane = frame.getContentPane();

		JButton accuracyButton = new JButton("Accuracy: " + String.format("%.0f", accuracy) + "%");
		JButton precisionButton = new JButton("Precision: " + String.format("%.0f", precision) + "%");
		JLabel label = new JLabel("Choose a new K value");
		
		// Slider
		int MIN = 2;
		int MAX = 25;
		int MAJOR_SPACING = 5;
		int MINOR_SPACING = 1;
		int INITIAL = K;
		
		JSlider slider = new JSlider(JSlider.HORIZONTAL, MIN, MAX, INITIAL);
		
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				if (!source.getValueIsAdjusting()) {
					int updatedValue = source.getValue();
					updatedValue = (updatedValue * 2) + 1;
					mainPanel.setKAndRedraw(updatedValue, fileName);
					mainPanel.repaint();
					accuracyButton.setText("Accuracy: " + String.format("%.0f", accuracy) + "%");
					precisionButton.setText("Precision: " + String.format("%.0f", precision) + "%");
				}
			}
		});
		
		slider.setMajorTickSpacing(MAJOR_SPACING);
		slider.setMinorTickSpacing(MINOR_SPACING);
		slider.setPaintTicks(true);
		
		Hashtable sliderLabels = new Hashtable();
		sliderLabels.put(new Integer(MIN), new JLabel("2"));
		sliderLabels.put(new Integer(MAJOR_SPACING), new JLabel("5"));
		sliderLabels.put(new Integer(10), new JLabel("10"));
		sliderLabels.put(new Integer(15), new JLabel("15"));
		sliderLabels.put(new Integer(20), new JLabel("20"));
		sliderLabels.put(new Integer(MAX), new JLabel("25"));
		slider.setLabelTable(sliderLabels);
		slider.setPaintLabels(true);
		
		
		contentPane.add(accuracyButton, BorderLayout.LINE_START);
		contentPane.add(precisionButton, BorderLayout.LINE_END);
		contentPane.add(label, BorderLayout.PAGE_END);
		contentPane.add(slider, BorderLayout.AFTER_LAST_LINE);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(mainPanel, BorderLayout.PAGE_START);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	/* The main method runs createAndShowGui */
	public static void main(String[] args) {
		int K = getInput(); // A value of K selected
		String fileName = "titanic.csv"; // TODO: Change this to titanic.csv

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGui(K, fileName);
			}
		});
	}

	public static int getInput() {
		boolean isValid = false;
		int result = -1;

		System.out.println("Welcome to the Graphical KNNPredictor.");
		Scanner scanner = new Scanner(System.in);

		do {
			System.out.println("Enter an odd integer (k-value): ");

			if (scanner.hasNextInt()) {
				result = scanner.nextInt();
			} else {
				System.out.println("Error: Invalid input. Please enter an integer.");
				scanner.nextLine();
				continue;
			}

			if (result % 2 == 0) {
				System.out.println("Error: Integer entered is even.");
			} else {
				isValid = true;
			}
		} while (!isValid);

		scanner.close();

		return result;
	}
}
