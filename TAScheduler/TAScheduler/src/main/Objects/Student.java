package Objects;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import Objects.Course;

public class Student implements Comparable<Student> {

	private Map<DayOfWeek, ArrayList<Integer>> hoursAvailable;
	private String firstName;
	private String lastName;
	private ArrayList<Course> classesTaken;
	private Quarter gradQuarter;
	private int gradYear;
	private int studentID;
	private int taHours = 0;
	
	public Student(String first, String last, int id, Quarter gradQ, int gradY) {
		
		firstName = first;
		lastName = last;
		studentID = id;
		gradQuarter = gradQ;
		gradYear = gradY;
		
		classesTaken = new ArrayList<Course>();
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
	
	public String toString() {
		
		String string = "Name: " + firstName + " " + lastName + "\n" +
						"Student ID: " + studentID + "\n" +
						"Graduates: " + gradQuarter + " " + gradYear + "\n";
		
		DayOfWeek[] days = DayOfWeek.values();
		for (int i = 0; i < days.length; i++) 
			string += days[i] + " availability: " + hoursAvailable.get(days[i]) + "\n";
		
		return string;
		
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

	public ArrayList<Course> getClassesTaken() {
		
		return classesTaken;
	}

	public void setClassesTaken(ArrayList<Course> classesTaken) {
		
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
}
