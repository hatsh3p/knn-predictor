public class DataPoint {
	
	// Step 3: Create a data point class with the following instance variables
	private double field1;
	private double field2;
	private String label;
	private boolean isATestDataPoint;
	
	// Constructor, no arg
	public DataPoint() {
		this.field1 = 0;
		this.field2 = 0;
		this.label = "";
		this.isATestDataPoint = false; 
	}

	// Constructor with four arguments
	public DataPoint(double field1, double field2, String label, boolean isATestDataPoint) {
		this.field1 = field1;
		this.field2 = field2;
		this.label = label;
		this.isATestDataPoint = isATestDataPoint;
	}

	// Setters and Getters
	public double getField1() {
		return field1;
	}

	public void setField1(double field1) {
		this.field1 = field1;
	}

	public double getField2() {
		return field2;
	}

	public void setField2(double field2) {
		this.field2 = field2;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isATestDataPoint() {
		return isATestDataPoint;
	}

	public void setATestDataPoint(boolean isATestDataPoint) {
		this.isATestDataPoint = isATestDataPoint;
	}
	
	
	
}