package Objects;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Map;

public class Course implements Comparable<Course> {

	private Map<DayOfWeek, ArrayList<Integer>> timeOffered;
	private int credits = 0;
	private int maxTAcount = 0;
	private int courseNumber;
	private ArrayList<Instructor> instructors;
	
	public Course(ArrayList<Instructor> inst, int suffix, int cred, Map<DayOfWeek, ArrayList<Integer>> times) {
		
		instructors = inst;
		credits = cred;
		timeOffered = times;
		courseNumber = suffix;
	}
	
	public String toString() {
		
		String string = "Class: CS " + courseNumber + "\n" +
						"Taught by: " + instructors + "\n";
		
		DayOfWeek[] days = DayOfWeek.values();
		for (int i = 0; i < days.length; i++) {
			string += days[i] + " times: "; 
			for (int j = 0; j < timeOffered.get(days[i]).size(); j++)
				string += timeOffered.get(days[i]).get(j) + ", ";
			string += "\n";
		}
		return string;
	}
	
	public boolean equals(Course c) {
		
		if (this.hashCode() - c.hashCode() == 0)
			return true;
		
		else 
			return false;
	}
	
	public int compareTo(Course c) {
		
		return courseNumber - c.getCourseNumber();
	}

	public Map<DayOfWeek, ArrayList<Integer>> getTimeOffered() {
		
		return timeOffered;
	}

	public void setTimeOffered(Map<DayOfWeek, ArrayList<Integer>> timeOffered) {
		
		this.timeOffered = timeOffered;
	}

	public int getCredits() {
		
		return credits;
	}

	public void setCredits(int credits) {
		
		this.credits = credits;
	}

	public int getMaxTAcount() {
		
		return maxTAcount;
	}

	public void setMaxTAcount(int maxTAcount) {
		
		this.maxTAcount = maxTAcount;
	}

	public int getCourseNumber() {
		
		return courseNumber;
	}

	public void setCourseNumber(int courseNumber) {
		
		this.courseNumber = courseNumber;
	}

	public ArrayList<Instructor> getInstructors() {
		
		return instructors;
	}

	public void setInstructors(ArrayList<Instructor> instructors) {
		
		this.instructors = instructors;
	}
}
