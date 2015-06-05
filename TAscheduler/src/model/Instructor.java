package model;

import java.io.Serializable;
import java.util.ArrayList;

import org.eclipse.swt.graphics.Color;

public class Instructor implements Serializable {

	private String firstName;
	private String lastName;
	private char middleInitial;
	private Color color;

	public Instructor (String first, String last, char middle) {
		
		firstName = first;
		lastName = last;
		middleInitial = middle;
	}
	
	@Override
	public boolean equals(Object i)
	{
		if (!(i instanceof Instructor))
			return false;
		
		if (((Instructor)i).getFirstName().equals(firstName) && ((Instructor)i).getLastName().equals(lastName) && ((Instructor)i).getMiddleInitial() == middleInitial)
			return true;
		
		return false;
	}
	
	public String toString() {
		
		return firstName + "" + middleInitial + "" + lastName;
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

	public char getMiddleInitial() {
		
		return middleInitial;
	}

	public void setMiddleInitial(char middle) {
		
		middleInitial = middle;
	}
	
	public Color getColor() {
		
		return color;
	}

	public void setColor(Color color) {
		
		this.color = color;
	}
}
