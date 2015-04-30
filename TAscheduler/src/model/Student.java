package model;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import model.Course;

public class Student implements Comparable<Student>, Serializable {

	private Map<DayOfWeek, ArrayList<Integer>> hoursAvailable;
	private String firstName;
	private String lastName;
	private HashMap<Course, Grade> classesTaken;
	private Quarter gradQuarter;
	private int gradYear;
	private String email;
	private int studentID;
	private int taHours = 0; // Hours that they have been scheduled for so far
	
	public Student(String first, String last, int id, Quarter gradQ, int gradY, String email) {
		
		firstName = first;
		lastName = last;
		studentID = id;
		gradQuarter = gradQ;
		gradYear = gradY;
		this.email = email;
		
		classesTaken = new HashMap<Course, Grade>();
		hoursAvailable = new TreeMap<DayOfWeek, ArrayList<Integer>>();
		
	}
	
	public int compareTo(Student other) {
		
		if (gradYear - other.getGradYear() != 0)
			return gradYear - other.getGradYear();
		
		else if (gradQuarter.getValue() != other.getGradQuarter().getValue())
			return (gradQuarter.getValue() - other.getGradQuarter().getValue());

		else 
			return studentID - other.getStudentID();
	
	}
	
	public boolean equals(Student other) {
		System.out.println("Equal");
		if (gradYear - other.getGradYear() != 0)
			return false;
		
		else if (gradQuarter.getValue() != other.getGradQuarter().getValue())
			return false;

		else if (studentID - other.getStudentID() != 0)
			return false;
		
		else 
			return true;
	}
	
	public String toString() {
		
		String string = "Name: " + firstName + " " + lastName + "\n" +
						"Student ID: " + studentID + "\n" +
						"Graduates: " + gradQuarter + " " + gradYear + "\n";
		
		DayOfWeek[] days = DayOfWeek.values();
		for (int i = 0; i < days.length; i++) 
			string += days[i] + " availability: " + hoursAvailable.get(days[i]) + "\n";
		
		return string;
		
	}

	public void addCourse(Course c, Grade grade) {
	
		classesTaken.put(c, grade);
	}
	
	public void removeCourse(Course c) {
		
		classesTaken.remove(c);
	}
	
	public Grade getGrade(Course c) {
		
		return classesTaken.get(c);
	}
	
	public Map<DayOfWeek, ArrayList<Integer>> getHoursAvailable() {
		
		return hoursAvailable;
	}

	public void setHoursAvailable(Map<DayOfWeek, ArrayList<Integer>> hoursAvailable) {
		
		this.hoursAvailable = hoursAvailable;
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

	public HashMap<Course, Grade> getClassesTaken() {
		
		return classesTaken;
	}

	public void setClassesTaken(HashMap<Course, Grade>  classesTaken) {
		
		this.classesTaken = classesTaken;
	}

	public Quarter getGradQuarter() {
		
		return gradQuarter;
	}

	public void setGradQuarter(Quarter gradQuarter) {
		
		this.gradQuarter = gradQuarter;
	}

	public int getGradYear() {
		
		return gradYear;
	}

	public void setGradYear(int gradYear) {
		
		this.gradYear = gradYear;
	}

	public int getStudentID() {
		
		return studentID;
	}

	public void setStudentID(int studentID) {
		
		this.studentID = studentID;
	}

	public int getTAHours() {
		
		return taHours;
	}

	public void setTAHours(int taHours) {
		
		this.taHours = taHours;
	}

	public String getEmail() {
		
		return email;
	}

	public void setEmail(String email) {
		
		this.email = email;
	}

	public int getTaHours() {
		
		return taHours;
	}

	public void setTaHours(int taHours) {
		
		this.taHours = taHours;
	}
}
