package model;

import java.io.Serializable;

public enum Quarter implements Serializable {
	
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
		if (str.equals("winter") || str.equals("Winter"))
			return WINTER;
		else if (str.equals("spring") || str.equals("Spring"))
			return SPRING;
		else if (str.equals("summer") || str.equals("Summer"))
			return SUMMER;
		else if (str.equals("fall") || str.equals("Fall"))
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
