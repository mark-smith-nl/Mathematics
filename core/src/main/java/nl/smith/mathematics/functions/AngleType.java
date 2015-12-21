package nl.smith.mathematics.functions;

public enum AngleType {
	DEG("A degree (in full, a degree of arc, arc degree, or arcdegree), is a measurement of plane angle, representing 1â�„360 of a full rotation"),
	
	RAD(
			"An angle's measurement in radians is numerically equal to the length of a corresponding arc of a unit circle. One radian is just under 57.3 degrees (when the arc length is equal to the radius)"),
	
	GRAD("The gradian is a unit of plane angle, equivalent to 1â�„400 of a turn");
	
	private final String description;
	
	private AngleType(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
}
