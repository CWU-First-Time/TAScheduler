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
					Set<Course> taken = next.getClassesTaken().keySet();
					
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
		
		for (int j = 0; j < 10; j++) {

			TreeMap<DayOfWeek, Integer> times = new TreeMap<DayOfWeek, Integer>();

			for (int k = 0; k < 7; k++) {
				
				times.put(days[k], j);
			}

			Course stuff = new Course(new Instructor("Jean", "Joseph", null), 1, 400 + j, 4, 200+j);
			stuff.setTimeOffered(times);
			classes.add(stuff);
		}
		
		for (int i = 0; i < classes.size(); i++)
			possibleTAs.put(classes.get(i), new PriorityQueue<Student>());

		Random random = new Random();
		Quarter[] quarters = Quarter.values();	
		
		for (int i = 0; i < quarters.length; i++) {

			for (int j = 0; j < 20; j++) {

				char[] first = new char[random.nextInt(10) + 2];
				for (int k = 0; k < first.length; k++) {
					
					first[k] = (char)(random.nextInt(26) + 97);
				}
				
				char[] last = new char[random.nextInt(10) + 2];
				for (int k = 0; k < last.length; k++) {
					
					last[k] = (char)(random.nextInt(26) + 97);
				}
				
				Student student = new Student(new String(first), new String(last), (20000000 + random.nextInt(100)), quarters[i], 2100-random.nextInt(3), "Benjamin@Netanyahu.com");
				TreeMap<DayOfWeek, ArrayList<Integer>> times = new TreeMap<DayOfWeek, ArrayList<Integer>>();
				
				ArrayList<Integer> hours = new ArrayList<Integer>();

				for (int l = 0; l < 6; l++)
					hours.add(random.nextInt(24));

				for (int k = 0; k < days.length; k++) {

					times.put(days[k], hours);
				}

				student.setHoursAvailable(times);
				students.add(student);

			}
		}
		
		LinkedList<Student> studse = new LinkedList<Student>(students);
		
		for (int i = 0; i < students.size(); i++) {
			for (int j = 0; j < classes.size(); j++) {
				Grade grade;
				switch (random.nextInt(5)) {
					case 0:
						grade = Grade.A;
						break;
					case 1:
						grade = Grade.B;
						break;
					case 2:
						grade = Grade.C;
						break;
					case 3:
						grade = Grade.D;
						break;
					default:
						grade = Grade.F;
						break;
				}
				studse.get(i).addCourse(classes.get(j), grade);
			}
		}

		PriorityQueue<Student> studs = new PriorityQueue<Student>(students);

		while (!studs.isEmpty())
			possibleCourses.put(studs.peek(), getPossibleCourses(studs.remove()));
		
		for (int i = 0; i < classes.size(); i++)
			courseQueue.add(classes.get(i));
		
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
		
		return classes.add(c);
	}
	
	public boolean addInstructor(Instructor i) {
		
		return instructors.add(i);
	}
	
	public boolean removeStudent(Student stud) {
		
		return students.remove(stud);
	}
	
	public boolean removeCourse(Course c) {
		
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