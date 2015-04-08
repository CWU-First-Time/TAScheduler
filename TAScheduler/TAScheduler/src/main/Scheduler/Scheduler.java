package Scheduler;

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

import Objects.Course;
import Objects.Instructor;
import Objects.Quarter;
import Objects.Student;

public class Scheduler {

	private static final int MAX_TA_HOURS = 1;
	private static PriorityQueue<Student> students;
	private static PriorityQueue<Course> courseQueue;
	private static ArrayList<Course> classes;
	private static Map<Course, ArrayList<Student>> possibleTAs;
	private static Map<Student, ArrayList<Course>> possibleCourses;
	private static DayOfWeek[] days = DayOfWeek.values();

	public static ArrayList<Course> getPossibleCourses(Student stud) {

		ArrayList<Course> classList = new ArrayList<Course>(classes);

		for (int i = 0; i < classes.size(); i++) {

			Course temp = classes.get(i);

			for (int j = 0; j < days.length; j++) {
				for (int k = 0; k < temp.getTimeOffered().get(days[k]).size(); k++) {

					if (!stud.getHoursAvailable().get(days[j]).contains(temp.getTimeOffered().get(days[j]).get(k)))
						classList.remove(temp);
				}

			}

			if (classList.contains(temp) && (!possibleTAs.get(classes.get(i)).contains(stud)))
				possibleTAs.get(classes.get(i)).add(stud);
		}

		return classList;
	}
	
	private static ArrayList<Student> schedule(Course c, int maxTAs) {
		
		int courseIndex = classes.indexOf(c);
		ArrayList<Student> taList = new ArrayList<Student>(possibleTAs.get(classes.get(courseIndex)));
		
		if (taList.size() <= maxTAs)
			return taList;
		
		ArrayList<Integer> compatibleCourseCount = new ArrayList<Integer>();
		while (compatibleCourseCount.size() < taList.size()) compatibleCourseCount.add(0);
		PriorityQueue<Integer> options = new PriorityQueue<Integer>();
		
		for (int i = 0; i < compatibleCourseCount.size(); i++) {
			for (int j = 0; j < classes.size(); j++) {
				
				if (possibleTAs.get(classes.get(j)).contains(taList.get(i)))
					compatibleCourseCount.set(i, compatibleCourseCount.get(i) + 1);
			}
			
			options.add(compatibleCourseCount.get(i));
		}

		while (taList.size() > maxTAs) {
			
			if (options.peek() == null)
				break;
			
			for (int i = 0; i < taList.size(); i++) {
				
				int highest = options.peek();
				if (compatibleCourseCount.get(i) >= highest) {
					options.remove();
					taList.remove(i);
					compatibleCourseCount.remove(i);
					break;
				}
			}
		}

		for (int i = 0; i < taList.size(); i++) {
			
			taList.get(i).setTAHours(taList.get(i).getTAHours() + 1);
			
			if (taList.get(i).getTAHours() == MAX_TA_HOURS) {
				
				Student student = taList.get(i);
				
				for (int j = 0; j < possibleCourses.get(student).size(); j++)
					possibleTAs.get(possibleCourses.get(student).get(j)).remove(student);
				
				possibleCourses.remove(student);
				
			} 
			
		}
		
		return taList;
	}

	public static void main(String[] args) {

		possibleCourses = new TreeMap<Student, ArrayList<Course>>();
		students = new PriorityQueue<Student>();
		courseQueue = new PriorityQueue<Course>(new CourseComparator());

		Random random = new Random();
		Quarter[] quarters = Quarter.values();
		for (int i = 0; i < quarters.length; i++) {

			for (int j = 0; j < 10; j++) {

				Student student = new Student("Jimmy", "John", (20000000 + random.nextInt(100)), quarters[i], 2100 - random.nextInt(25));
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

			ArrayList<Instructor> instructors = new ArrayList<Instructor>();
			instructors.add(new Instructor("Jean", "Joseph", null));
			TreeMap<DayOfWeek, ArrayList<Integer>> times = new TreeMap<DayOfWeek, ArrayList<Integer>>();

			for (int k = 0; k < 7; k++) {
				ArrayList<Integer> stop = new ArrayList<Integer>();
				stop.add(j);
				times.put(days[k], stop);
			}

			Course stuff = new Course(instructors, 400 + j, 4, times);
			classes.add(stuff);
		}

		possibleTAs = new TreeMap<Course, ArrayList<Student>>();
		for (int i = 0; i < classes.size(); i++)
			possibleTAs.put(classes.get(i), new ArrayList<Student>());

		PriorityQueue<Student> studs = new PriorityQueue<Student>(students);

		while (!studs.isEmpty())
			possibleCourses.put(studs.peek(), getPossibleCourses(studs.remove()));

		for (int i = 0; i < classes.size(); i++)
			courseQueue.add(classes.get(i));
		
		while (!courseQueue.isEmpty())
			System.out.println("\nFor: " + courseQueue.peek() + "\n" + schedule(courseQueue.remove(), 4) + "\n\n");;

	}
	
	private static class CourseComparator implements Comparator<Course> {

		public int compare(Course c1, Course c2) {

			return possibleTAs.get(c1).size() - possibleTAs.get(c2).size();
			
		}
		
	}

}
