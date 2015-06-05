package scheduler;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import model.Course;
import model.Grade;
import model.Instructor;
import model.Quarter;
import model.Student;
import model.Which92;


public class Scheduler implements Serializable {

	private final int MAX_TA_HOURS = 1;
	private PriorityQueue<Student> students;
	private PriorityQueue<Course> courseQueue;
	private ArrayList<Course> classes;
	private ArrayList<Instructor> instructors;
	private Map<Course, PriorityQueue<Student>> possibleTAs;
	private Map<Student, ArrayList<Course>> possibleCourses;
	private DayOfWeek[] days = DayOfWeek.values();
	private boolean[] studentColumnsGrayed;

	private ArrayList<Course> getPossibleCourses(Student stud) {

		ArrayList<Course> classList = new ArrayList<Course>(classes);

		for (int i = 0; i < classes.size(); i++) {

			Course temp = classes.get(i);

			for (int j = 0; j < days.length; j++) {

				if (!stud.getHoursAvailable().get(days[j]).contains(temp.getTimeOffered().get(days[j])))
					classList.remove(temp);

			}

			if (classList.contains(temp) && (!possibleTAs.get(classes.get(i)).contains(stud)))
				possibleTAs.get(classes.get(i)).add(stud);
		}

		return classList;
	}
	
	public ArrayList<Student> schedule(Course c, int maxTAs, boolean preReq) {

		ArrayList<Student> finalList = new ArrayList<Student>();
		PriorityQueue<Student> studentsTaken = possibleTAs.get(c);

		while (finalList.size() < maxTAs) {
			
			PriorityQueue<Student> possibleStuds = new PriorityQueue<Student>(); 	
		
			if (preReq) {
				while (!studentsTaken.isEmpty()) {
				
					Student next = studentsTaken.remove();
					Set<Integer> taken = next.getClassesTaken().keySet();
					
					for (int i = 0; i < taken.size(); i++) {
						if (taken.contains(c)) {

							possibleStuds.add(next);
							break;
						}
					}
				}
			
			} else
				possibleStuds = new PriorityQueue<Student>(studentsTaken);

			LinkedList<Student> graduateNext = new LinkedList<Student>();
			
			if (possibleStuds.peek() != null) {
				
				while (graduateNext.isEmpty() ||
					   (!possibleStuds.isEmpty() &&
					   (graduateNext.peek().getGradYear()  == possibleStuds.peek().getGradYear() && 
				        graduateNext.peek().getGradQuarter() == possibleStuds.peek().getGradQuarter()))) 
				
					graduateNext.push(possibleStuds.remove());
				
			} else
				break;

			int[] compatibleCourseCount = new int[graduateNext.size()];
					
			for (int i = 0; i < graduateNext.size(); i++) {
				for (int j = 0; j < classes.size(); j++) {
					
					if (possibleTAs.get(classes.get(j)).contains(graduateNext.get(i)))
						compatibleCourseCount[i]++;
					
				}
				
			}
			
			// Un-optimized
			int nextLowest = 900000000;
			for (int i = 0; i < compatibleCourseCount.length; i++) {
				
				if (compatibleCourseCount[i] < nextLowest)
					nextLowest = compatibleCourseCount[i];
			}
			
			for (int i = 0; i < graduateNext.size(); i++) {
				
				if (compatibleCourseCount[i] <= nextLowest) {
					studentsTaken.remove(graduateNext.get(i));
					finalList.add(graduateNext.get(i));
					break;
				}
				
			}

		}

		for (int i = 0; i < finalList.size(); i++) {
			
			finalList.get(i).setTAHours(finalList.get(i).getTAHours() + 1);
			
			if (finalList.get(i).getTAHours() == MAX_TA_HOURS) {
				
				Student student = finalList.get(i);
				
				for (int j = 0; j < possibleCourses.get(student).size(); j++)
					possibleTAs.get(possibleCourses.get(student).get(j)).remove(student);
				
			} 
			
		}
		
		return finalList;
	}

	public Scheduler() {

		students = new PriorityQueue<Student>();
		courseQueue = new PriorityQueue<Course>(new CourseComparator());
		classes = new ArrayList<Course>();
		instructors = new ArrayList<Instructor>();
		
		possibleCourses = new TreeMap<Student, ArrayList<Course>>();
		
		possibleTAs = new TreeMap<Course, PriorityQueue<Student>>();
		
	
			
		
	}
	
	public LinkedList<Student> getStudents() {
		
		return new LinkedList<Student>(students);
	}
	
	public ArrayList<Course> getCourses() {
		
		return classes;
	}
	
	public boolean addStudent(Student stud) {
		
		return students.add(stud);
	}
	
	public boolean addCourse(Course c) {
		
		boolean[]temp = new boolean[studentColumnsGrayed.length+1];
		for (int i = 0; i < studentColumnsGrayed.length; i++)
			temp[i] = studentColumnsGrayed[i];
		studentColumnsGrayed = temp;
		return classes.add(c);
	}
	
	public boolean addInstructor(Instructor ins) {
		
		for (Instructor i : instructors)
		{
			if (i.equals(ins))
			{
				return false;
			}
		}
		
		return instructors.add(ins);
	}
	
	public boolean removeStudent(Student stud) {
		
		return students.remove(stud);
	}
	
	public boolean removeCourse(Course c) {
		
		ArrayList<Boolean> grayed = new ArrayList<Boolean>();
		for (int i = 0; i < studentColumnsGrayed.length; i++)
			grayed.add(studentColumnsGrayed[i]);
		
		int index = classes.indexOf(c);
		grayed.remove(index);
		studentColumnsGrayed = new boolean[grayed.size()];	
		for (int i = 0; i < grayed.size(); i++)
			studentColumnsGrayed[i] = grayed.get(i);
		return classes.remove(c);
	}
	
	public boolean removeInstructor(Instructor i) {
		
		return instructors.remove(i);
	}
	
	public ArrayList<Instructor> getInstructors() {
		
		return instructors;
	}

	public void setInstructors(ArrayList<Instructor> instructors) {
		
		this.instructors = instructors;
	}
	
 	private class CourseComparator implements Comparator<Course>, Serializable {

		public int compare(Course c1, Course c2) {

			if (possibleTAs != null) 
				return possibleTAs.get(c1).size() - possibleTAs.get(c2).size();
			else
				return 0;
			
		}
		
	}

	public boolean[] getStudentColumnsGrayed() {
		
		return studentColumnsGrayed;
	}

	public void setStudentColumnsGrayed(boolean[] studentColumnsGrayed) {
		
		this.studentColumnsGrayed = studentColumnsGrayed;
	}

}