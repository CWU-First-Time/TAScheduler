package model;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

public class Course implements Comparable<Course>, Serializable {

	private Map<DayOfWeek, Integer> timeOffered;
	private int maxTAcount = 0;
	private int courseNumber;
	private int section;
	private int roomNumber;
	private Instructor instructor;
	private String title;

	public Course(Instructor inst, int sec, int courseNum, int room) {
		
		instructor = inst;
		section = sec;
		courseNumber = courseNum;
		roomNumber = room;
		
		timeOffered = new HashMap<DayOfWeek, Integer>();
	}
	
	public String toString() {
		
		return "CS " + courseNumber + "." + String.format("%03d", section) + ": " + title;
		
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

	public Map<DayOfWeek, Integer> getTimeOffered() {
		
		return timeOffered;
	}

	public void setTimeOffered(Map<DayOfWeek, Integer> timeOffered) {
		
		this.timeOffered = timeOffered;
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

	public Instructor getInstructor() {
		
		return instructor;
	}

	public void setInstructor(Instructor instructor) {
		
		this.instructor = instructor;
	}

	public int getSection() {
		
		return section;
	}

	public void setSection(int section) {
		
		this.section = section;
	}

	public int getRoomNumber() {
		
		return roomNumber;
	}

	public void setRoomNumber(int roomNumber) {
		
		this.roomNumber = roomNumber;
	}
	
	
	public String getTitle() {
		
		return title;
	}

	public void setTitle(String title) {
		
		this.title = title;
	}
}
