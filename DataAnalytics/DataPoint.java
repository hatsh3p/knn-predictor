public class DataPoint {
	
	private double field1; // age (column 5)
	private double field2; // fare (column 6)
	private int label; // initially, label was a String type
	private boolean isATestDataPoint;
	
	// Constructor, no arg
	public DataPoint() {
		this.field1 = 0;
		this.field2 = 0;
		this.label = 0; 
		this.isATestDataPoint = false; 
	}

	// Constructor with four arguments
	public DataPoint(double field1, double field2, int label, boolean isATestDataPoint) {
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

	public int getLabel() {
		return label;
	}

	public void setLabel(int label) {
		this.label = label;
	}

	public boolean isATestDataPoint() {
		return isATestDataPoint;
	}

	public void setATestDataPoint(boolean isATestDataPoint) {
		this.isATestDataPoint = isATestDataPoint;
	}
	
	public String toString() {
		return "Field 1: " + this.field1 +
				", Field 2: " + this.field2 +
				", Label: " + this.label +
				", IsATestDataPoint: " + this.isATestDataPoint;
	}
	
}