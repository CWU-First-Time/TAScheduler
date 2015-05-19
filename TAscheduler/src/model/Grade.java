package model;

import java.io.Serializable;

public enum Grade implements Serializable {

	A(0),
	
	A_MINUS(1),
	
	B_PLUS(2),
	
	B(3),
	
	B_MINUS(4), 
	
	C_PLUS(5),
	
	C(6),
	
	C_MINUS(7),
	
	D_PLUS(8),
	
	D(9), 
	
	D_MINUS(10),
	
	F(11);
	
	private int value;
	
	private Grade(int value)  {
		
		this.value = value;
		
	}
	
	public int getValue() {
		
		return value;
	}

	public static Grade value(String s)
	{
		switch (s) {
			case "A":
				return A;
		
			case "A-":
				return A_MINUS;
				
			case "B+":
				return B_PLUS;
				
			case "B":
				return B;
				
			case "B-":
				return B_MINUS;
				
			case "C+":
				return C_PLUS;
				
			case "C":
				return C;
				
			case "C-":
				return C_MINUS;
				
			case "D+":
				return D_PLUS;
				
			case "D":
				return D;
				
			case "D-":
				return D_MINUS;
				
			default:
				return F;
		}
	}
	
	public String toString() {
		
		switch (this.value) {
			
			case 0:
				return "A";
				
			case 1:
				return "A-";
				
			case 2:
				return "B+";
				
			case 3:
				return "B";
				
			case 4:
				return "B-";
				
			case 5:
				return "C+";
			
			case 6:
				return "C";
				
			case 7:
				return "C-";
				
			case 8:
				return "D+";
				
			case 9:
				return "D";
				
			case 10:
				return "D-";
				
			case 11:
				return "F";
		}
		
		return "";
	}
}
