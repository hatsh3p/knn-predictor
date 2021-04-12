import java.awt.Container;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
	private static double accuracy = -0.1;
	private static double precision = -0.1;
	
	public static void main(String[] args) {
		
		System.out.println("Welcome to the KNN Predictor. ");
		
		int k = getInput();
		
		Predictor knnPredictor = new KNNPredictor(k);
		
		ArrayList<DataPoint> dataArrayList = new ArrayList<DataPoint>();
		
		dataArrayList = knnPredictor.readData("titanic.csv");
		
		accuracy = knnPredictor.getAccuracy(dataArrayList) * 100;
		precision = knnPredictor.getPrecision(dataArrayList) * 100;
		
		
		// Call function that displays user-interface
		SwingUtilities.invokeLater(
		          new Runnable() { public void run() { initAndShowGUI(); } }
		        );
	}
	
	public static int getInput() {
		boolean isValid = false;
		int result = -1;
		
		Scanner scanner = new Scanner(System.in);
		
		do {
			System.out.println("Enter an odd integer: ");
			
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
	
	private static void initAndShowGUI() {
		// Create a JFrame with a set size
		JFrame dataFrame = new JFrame();
		dataFrame.setSize(500, 500);
		
		// Create a content pane with a GridLayout and specified size
		Container contentPane = dataFrame.getContentPane();
		contentPane.setLayout(new GridLayout(1, 2));
		contentPane.setPreferredSize(new Dimension(400, 200));
		
		// Create and add buttons for accuracy and precision
		JButton accuracyButton = new JButton("Accuracy: " + String.format("%.0f", accuracy) + "%");
		JButton precisionButton = new JButton("Precision: " + String.format("%.0f", precision) + "%");
		
		contentPane.add(accuracyButton);
		contentPane.add(precisionButton);
		
		// Realize and display window with a title
		dataFrame.pack();
		dataFrame.setVisible(true);
		dataFrame.setTitle("KNN Predictor calculations: ");
		
	}
}