package model;

import java.io.Serializable;

public enum Grade implements Serializable {

	A(0),
	
	B(1),
	
	C(2),
	
	D(3), 
	
	F(4);
	
	private int value;
	
	private Grade(int value)  {
		
		this.value = value;
		
	}
	
	public int getValue() {
		
		return value;
	}
	
	public String toString() {
		
		switch (this.value) {
			
			case 0:
				return "A";
				
			case 1:
				return "B";
			
			case 2:
				return "C";
				
			case 3:
				return "D";
				
			case 4:
				return "F";
		}
		
		return "";
	}
}
