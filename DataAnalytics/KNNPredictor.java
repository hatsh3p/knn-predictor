import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class KNNPredictor extends Predictor { //KNN stands for K-nearest-neighbor
	
	// Constants for reading .csv file
	private static int FARE_INDEX = 6; 
	private static int AGE_INDEX = 5;
	private static int SURVIVAL_DATA_INDEX = 1;
	
	private static int SURVIVED = 0;
	private static int DECEASED = 1;

	private ArrayList<DataPoint> dataSet;
	
	private int numOfPassengersSurvived = 0;
	private int numOfPassengersDeceased = 0;
	
	private int k;
	
	public KNNPredictor(int k) {
		this.k = k;
	}

	public ArrayList<DataPoint> readData(String filename){  
		
		ArrayList<DataPoint> result = new ArrayList<DataPoint>();
		
		File file = new File(filename);
		Scanner scanner;
		
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("File not found. Empty array list returned");
			return result;
		}
	
		// Reset count
		numOfPassengersSurvived = 0;
		numOfPassengersDeceased = 0;
		
		// Track line number to report errors
		int lineNumber = 0;
		
		// Track incomplete or corrupted records
		int unusedRecords = 0;
		
		// Use to assign data as either test or train
		Random random = new Random();
		
		// Skips first line of file (i.e. column labels)
		scanner.nextLine(); 
		
		while (scanner.hasNextLine()) {
			lineNumber++; 
			
			List<String> records = getRecordFromLine(scanner.nextLine());
			
			// Ignore lines that do not are missing fields
			if(records.size() < 7) {
				unusedRecords++;
				continue;
			}

			String stringAge = records.get(AGE_INDEX);
			String stringFare = records.get(FARE_INDEX);
			String stringSurvivalData = records.get(SURVIVAL_DATA_INDEX);
			
			// Convert String fields collected into appropriate data types
			double age = 0.0;
			double fare = 0.0;
			int survived = 0; // set to ALIVE (0) or DECEASED (1)
			
			// Parse fields or ignore fields that cannot be parsed into appropriate types
			// Print statements used to view lines that are ignored
			try {
				age = Double.parseDouble(stringAge);
			} catch (NumberFormatException ex) {
				// System.err.println("Invalid age: " + stringAge + " at index: " + lineNumber);
				unusedRecords++;
				continue;
			}
			
			try {
				fare = Double.parseDouble(stringFare);
			} catch (NumberFormatException ex) {
				// System.err.println("Invalid fare: " + stringFare + " at index: " + lineNumber);
				unusedRecords++;
				continue;
			}
			
			try {
				survived = Integer.parseInt(stringSurvivalData);
			} catch (NumberFormatException ex) {
				// System.err.println("Invalid survival data: " + stringSurvivalData + " at index: " + lineNumber);
				unusedRecords++;
				continue;
			}
			
			boolean isTrainingData = random.nextDouble() < 0.9;
			
			if(isTrainingData) {
				if(survived == SURVIVED) {
					numOfPassengersSurvived++;
				} else {
					numOfPassengersDeceased++;
				}
			} 
	
			DataPoint dataPoint = new DataPoint(age, fare, survived, isTrainingData);
			result.add(dataPoint);
		}
		
		scanner.close();
		
		System.out.println("The data set provided contains: " +
				"\nTotal records in file: " + lineNumber + 
				"\nTotal unused records: " + unusedRecords + 
				"\nTotal training data: " + (numOfPassengersSurvived + numOfPassengersDeceased) +
				"\nTotal test data: " + ((lineNumber - unusedRecords) - (numOfPassengersSurvived + numOfPassengersDeceased)) +
				"\nThe training data contains: " +
				"\nNumber of passengers who survived: " + numOfPassengersSurvived + 
				"\nNumber of passengers deceased: " + numOfPassengersDeceased + "\n");

		dataSet = result;
		return result;
	}
	

	private List<String> getRecordFromLine(String line) {
		
		List<String> values = new ArrayList<String>();
		
		try (Scanner rowScanner = new Scanner(line)) {
			rowScanner.useDelimiter(",");
			while (rowScanner.hasNext()) {
				values.add(rowScanner.next());
			}
		}
		
		return values;
	}
	

	@Override
	public String test(DataPoint data) {

		if (!data.isATestDataPoint()) {
			return "Data provided is not a test data point."; 
		}
		
		int numOfTrainingData = numOfPassengersSurvived + numOfPassengersDeceased;
		
		// Create a 2D array for storing each the distance between each training point and the test point
		// and the label assigned to the training point
		Double[][] arr = new Double[numOfTrainingData][2]; 
		
		for (int row = 0; row < numOfTrainingData; row++) {
			DataPoint trainingData = dataSet.get(row);
			double distance = getDistance(data, trainingData);
			double label = trainingData.getLabel();
			arr[row][0] = distance;
			arr[row][1] = label;
		}
		
		// Code provided - Sort the array by distance in ascending order (smaller distance -> larger distance)
		java.util.Arrays.sort(arr, new java.util.Comparator<Double[]>() {
			public int compare(Double[] a, Double[] b) {
				return a[0].compareTo(b[0]);
			}
		});
	
		
		// Find labels of the first k elements
		// Return the label that occurs the most in String format ("1" or "0")
		int totalSurvived = 0;
		int totalDeceased = 0;
		
		for (int row = 0; row < k; row++) {
			double survivalRecord = arr[row][1];
			if (survivalRecord == SURVIVED) {
				totalSurvived++;
			} else {
				totalDeceased++;
			}
		}
		
		if(totalSurvived > totalDeceased) {
			return String.valueOf(SURVIVED);
		} else {
			return String.valueOf(DECEASED);
		}
	}

	
	private double getDistance(DataPoint p1, DataPoint p2) {
		double distance = 0.0;
		double num = 0.0;
		double x1 = p1.getField1();
		double y1 = p1.getField2();
		double x2 = p2.getField1();
		double y2 = p2.getField2();
		
		num = Math.pow((x2-x1), 2) + Math.pow((y2-y1), 2);
		distance = Math.sqrt(num);
		
		return distance;
	}
	
	// This class stores data for getAccuracy and getPrecision
	private static class DataAnalysis {
		int truePositive = 0;
		int falsePositive = 0;
		int falseNegative = 0;
		int trueNegative = 0;			
	}
	
	// This method updates a DataAnalysis object for use by getAccuracy() and getPrecision()
	private DataAnalysis analyze(ArrayList<DataPoint> data) {
		DataAnalysis result = new DataAnalysis();
		 
		for(DataPoint point : data) {
			// Skip non-test data
			if (!point.isATestDataPoint()) {
				continue;
			}
			
			// Read the label from the actual data set
			double actualLabel = point.getLabel();
			
			// Predict the label from the training data
			dataSet = data;
			double predictedLabel = Double.parseDouble(test(point));
			
			if (actualLabel == 1.0) {
				if (predictedLabel == 1.0) {
					result.truePositive++;
				} else {
					result.falsePositive++;
				}
			} else {
				if (predictedLabel == 1.0) {
					result.falseNegative++;
				} else {
					result.trueNegative++;
				}
			}
		}

		return result;
	}
	
	
	@Override
	public double getAccuracy(ArrayList<DataPoint> data) {
		
		DataAnalysis dataAnalysis = analyze(data);
		
		double totalResults = dataAnalysis.trueNegative + dataAnalysis.truePositive +
				dataAnalysis.falseNegative + dataAnalysis.falsePositive;
		
		if (totalResults == 0) {
			throw new ArithmeticException("Error: Division by zero");
		}
		
		double accuracy = (double) (dataAnalysis.truePositive + dataAnalysis.trueNegative) / (totalResults);
		
		return accuracy;
	}

	
	@Override
	public double getPrecision(ArrayList<DataPoint> data) {

		DataAnalysis dataAnalysis = analyze(data);
		
		double denominator = dataAnalysis.truePositive + dataAnalysis.falseNegative;
		
		if (denominator == 0) {
			throw new ArithmeticException("Error: Division by zero");
		}
		
		double precision = (double) (dataAnalysis.truePositive) / denominator;
		
		return precision;
	}	
}
