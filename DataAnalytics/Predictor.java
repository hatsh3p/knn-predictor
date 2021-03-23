import java.util.ArrayList;

// (4) Create an abstract class called Predictor with the following method signatures

public abstract class Predictor {
	
	// Method 1
	abstract ArrayList<DataPoint> readData(String filename);
	
	// Method 2
	abstract String test(DataPoint data);
	
	// Method 3
	abstract double getAccuracy(ArrayList<DataPoint> data);
	
	// Method 4
	abstract double getPrecision(ArrayList<DataPoint> data);
	
}