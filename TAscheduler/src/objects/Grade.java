package objects;

public enum Grade {

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
}
