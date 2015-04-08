package Objects;

import java.util.ArrayList;

public class Instructor {

	private String firstName;
	private String lastName;
	private ArrayList<Course> classesTaught;
	
	public Instructor (String first, String last, ArrayList<Course> classes) {
		
		firstName = first;
		lastName = last;
		classesTaught = classes;
	}
	
	public String toString() {
		
		return firstName + " " + lastName;
	}

	public String getFirstName() {
		
		return firstName;
	}

	public void setFirstName(String firstName) {
		
		this.firstName = firstName;
	}

	public String getLastName() {
		
		return lastName;
	}

	public void setLastName(String lastName) {
		
		this.lastName = lastName;
	}

	public ArrayList<Course> getClassesTaught() {
		
		return classesTaught;
	}

	public void setClassesTaught(ArrayList<Course> classesTaught) {
		
		this.classesTaught = classesTaught;
	}
}
