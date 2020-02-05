package my.springboot.property.domain;

public enum Country {
	CN("cn", "China"), USA("us", "america");
	
	private String shortName;
	private String longName;
	
	private Country(String shortName, String longName) {
		this.shortName = shortName;
		this.longName = longName;
	}
	
	public String shortName() {
		return this.shortName;
	}
	
	public String longName() {
		return this.longName;
	}
}
