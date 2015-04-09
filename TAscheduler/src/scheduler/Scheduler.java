package scheduler;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.TreeMap;

import objects.Course;
import objects.Instructor;
import objects.Quarter;
import objects.Student;

public class Scheduler {

	private static final int MAX_TA_HOURS = 1;
	private static PriorityQueue<Student> students;
	private static PriorityQueue<Course> courseQueue;
	private static ArrayList<Course> classes;
	private static Map<Course, PriorityQueue<Student>> possibleTAs;
	private static Map<Student, ArrayList<Course>> possibleCourses;
	private static DayOfWeek[] days = DayOfWeek.values();

	public static ArrayList<Course> getPossibleCourses(Student stud) {

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
	
	private static ArrayList<Student> schedule(Course c, int maxTAs, boolean preReq) {

		ArrayList<Student> finalList = new ArrayList<Student>();
		PriorityQueue<Student> studentsTaken = possibleTAs.get(c);

		while (finalList.size() < maxTAs) {
			
			PriorityQueue<Student> possibleStuds = new PriorityQueue<Student>(); 	
		
			if (preReq) {
				while (!studentsTaken.isEmpty()) {
				
					Student next = studentsTaken.remove();
					ArrayList<Course> taken = next.getClassesTaken();
					
					for (int i = 0; i < taken.size(); i++) {
						if (taken.get(i).equals(c)) {

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

	public static void main(String[] args) {

		possibleCourses = new TreeMap<Student, ArrayList<Course>>();
		students = new PriorityQueue<Student>();
		courseQueue = new PriorityQueue<Course>(new CourseComparator());

		Random random = new Random();
		Quarter[] quarters = Quarter.values();
		for (int i = 0; i < quarters.length; i++) {

			for (int j = 0; j < 20; j++) {

				Student student = new Student("Jimmy", "John", (20000000 + random.nextInt(100)), quarters[i], 2100);
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

		classes = new ArrayList<Course>();

		for (int j = 0; j < 10; j++) {

			TreeMap<DayOfWeek, Integer> times = new TreeMap<DayOfWeek, Integer>();

			for (int k = 0; k < 7; k++) {
				
				times.put(days[k], j);
			}

			Course stuff = new Course(new Instructor("Jean", "Joseph", null), 1, 400 + j, 4);
			stuff.setTimeOffered(times);
			classes.add(stuff);
		}

		possibleTAs = new TreeMap<Course, PriorityQueue<Student>>();
		for (int i = 0; i < classes.size(); i++)
			possibleTAs.put(classes.get(i), new PriorityQueue<Student>());

		PriorityQueue<Student> studs = new PriorityQueue<Student>(students);

		while (!studs.isEmpty())
			possibleCourses.put(studs.peek(), getPossibleCourses(studs.remove()));

		for (int i = 0; i < classes.size(); i++)
			courseQueue.add(classes.get(i));
		
		while (!courseQueue.isEmpty())
			System.out.println("\nFor: " + courseQueue.peek() + "\n" + schedule(courseQueue.remove(), 4, false) + "\n\n");;

	}
	
	private static class CourseComparator implements Comparator<Course> {

		public int compare(Course c1, Course c2) {

			return possibleTAs.get(c1).size() - possibleTAs.get(c2).size();
			
		}
		
	}

}