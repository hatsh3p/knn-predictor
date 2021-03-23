import java.awt.Container;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

// (1) Set up github repo
// (2) Create directory called DataAnalytics

// (6) Create a driver class

public class Main {
	
	static double accuracy = -1.0;
	static double precision = -1.0;
	
	public static void main(String[] args) {
	
		// Create an instance of DummyPredictor
		DummyPredictor dummyPredictor = new DummyPredictor();
		
		// Create an ArrayList that contains DataPoint
		ArrayList<DataPoint> dataArrayList = new ArrayList<DataPoint>();
		
		// Read data from file
		dataArrayList = dummyPredictor.readData("trainingdata.txt");
		
		// Test code: Print first value in ArrayList
		// System.out.println(dataArrayList.get(0));
		
		// Run test() method
		dummyPredictor.test(dataArrayList.get(0));
		
		// Run getAccuracy() and getPrecision() methods
		accuracy = dummyPredictor.getAccuracy(dataArrayList);
		precision = dummyPredictor.getPrecision(dataArrayList);
		
		// Call function that displays user-interface
		SwingUtilities.invokeLater(
		          new Runnable() { public void run() { initAndShowGUI(); } }
		        );
	}
	
	private static void initAndShowGUI() {
		// Create a JFrame with a set size
		JFrame dataFrame = new JFrame();
		dataFrame.setSize(500, 500);
		
		// Create a content pane with a GridLayout and specified size
		Container contentPane = dataFrame.getContentPane();
		contentPane.setLayout(new GridLayout(1, 2));
		contentPane.setPreferredSize(new Dimension(400, 200));
		
		// Create and add buttons for accuracy and precision
		JButton accuracyButton = new JButton("Accuracy: " + accuracy);
		JButton precisionButton = new JButton("Precision: " + precision);
		contentPane.add(accuracyButton);
		contentPane.add(precisionButton);
		
		// Realize and display window with a title
		dataFrame.pack();
		dataFrame.setVisible(true);
		dataFrame.setTitle("DummyPredictor's calculations: ");
		
	}
}