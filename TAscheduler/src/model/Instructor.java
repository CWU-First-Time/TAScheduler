package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Instructor implements Serializable {

	private String firstName;
	private String lastName;
	private String middleInitial;
	
	public Instructor (String first, String last, String middle) {
		
		firstName = first;
		lastName = last;
		middleInitial = middle;
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

	public String getMiddleInitial() {
		
		return middleInitial;
	}

	public void setMiddleInitial(String middle) {
		
		middleInitial = middle;
	}
}
