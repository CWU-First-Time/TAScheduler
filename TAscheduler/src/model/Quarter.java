package model;

public enum Quarter {
	
	WINTER(0),
	
	SPRING(1),
	
	SUMMER(2),
	
	FALL(3);
	
	private int value;
	
	private Quarter(int value) {
		
		this.value = value;
	}
	
	public int getValue() {
		
		return value;
	}
	
	public static Quarter getQuarter(String str) {
		if (str.equals("Winter"))
			return WINTER;
		else if (str.equals("Spring"))
			return SPRING;
		else if (str.equals("Summer"))
			return SUMMER;
		else if (str.equals("Fall"))
			return FALL;
		else
			return null;
	}
	
	public String toString() {
		
		switch (value) {
		
			case 0:
				return "Winter";
				
			case 1:
				return "Spring";
				
			case 2:
				return "Summer";
				
			case 3:
				return "Fall";
				
			default:
				return "";
				
		}
	}
}
