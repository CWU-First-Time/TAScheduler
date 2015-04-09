package objects;

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
	
	public String toString() {
		
		switch (value) {
		
			case 0:
				return "WINTER";
				
			case 1:
				return "SPRING";
				
			case 2:
				return "SUMMER";
				
			case 3:
				return "FALL";
				
			default:
				return null;
				
		}
	}
}
