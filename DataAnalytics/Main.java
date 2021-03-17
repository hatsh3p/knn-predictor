public class Main {
	
	public static void main(String[] args) {
		
		// Create and initialize training data set
		DataPoint[] trainingData = new DataPoint[4];
		trainingData[0] = new DataPoint(1.0, 2.0, "Green", false);
		trainingData[1] = new DataPoint(2.0, 3.0, "Green", false);
		trainingData[2] = new DataPoint(8.0, 12.0, "Blue", false);
		trainingData[3] = new DataPoint(6.0, 9.0, "Blue", false);
		
		// Create and initialize test data set
		DataPoint[] testData = new DataPoint[4];
		testData[0] = new DataPoint(3.0, 4.0, "", true);
		testData[1] = new DataPoint(1.0, 2.0, "", true);
		testData[2] = new DataPoint(5.0, 10.0, "", true);
		testData[3] = new DataPoint(7.0, 3.0, "", true);
		
		// Create an instance of DummyPredictor
		
	}
}