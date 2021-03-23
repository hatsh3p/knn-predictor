import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

// (5) Create a DummyPredictor class that inherits Predictor 
// Methods do not need to do anything meaningful at this point

public class DummyPredictor extends Predictor {

	// Method 1: readData()
	@Override
	public ArrayList readData(String filename) {
		System.out.println("readData() method called from DummyPredictor");
		
		ArrayList<DataPoint> dataArrayList = new ArrayList<DataPoint>();
		
		File file = new File(filename);

		try {
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				DataPoint dataPoint = new DataPoint();
				
				double field1 = scanner.nextDouble();
				double field2 = scanner.nextDouble();
				String label = scanner.next();
				boolean isATestDataPoint = scanner.nextBoolean();
		
				// Test Code to check if reading fields correctly
				// System.out.println(field1 + field2 + label + isATestDataPoint);

				dataPoint.setField1(field1);
				dataPoint.setField2(field2);
				dataPoint.setLabel(label);
				dataPoint.setATestDataPoint(isATestDataPoint);
				
				System.out.println("Point added--> " + dataPoint.toString());
				dataArrayList.add(dataPoint);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return dataArrayList;
	}

	// Method 2: test()
	@Override
	public String test(DataPoint data) {
		System.out.println("test() method called from DummyPredictor");
		if(data.isATestDataPoint()) {
			System.out.println("This is a test data point");
		} else {
			System.out.println("This is NOT a test data point. It has a label: " + data.getLabel());
		}
		return null;
	}

	// Method 3: getAccuracy()
	@Override
	public double getAccuracy(ArrayList<DataPoint> data) {
		System.out.println("getAccuracy() method called from DummyPredictor");
		return 0.0;
	}

	// Method 4: getPrecision()
	@Override
	public double getPrecision(ArrayList<DataPoint> data) {
		System.out.println("getPrecision() method called from DummyPredictor");
		return 0.0;
	}
	
}