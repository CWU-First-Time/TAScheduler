package model;

public enum Which92 {

	THREE92(0),
	FOUR92(1),
	PAID(2);
	
	private int value;
	
	private Which92(int value) {
		
		this.value = value;
	}
	
	public int getValue() {
		
		return value;
	}
	
	public String toString() {
		
		if (value < 2)
			return (value + 3) + "92";
		
		else
			return "PAID";
	}
	
	public static Which92 getWhich92(String w) {
		
		if (w.equals("392"))
			return Which92.THREE92;
		
		else if (w.equals("492"))
			return Which92.FOUR92;
		
		else 
			return Which92.PAID;
	}
}
