package GUI.schedule;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;

import model.Course;
import model.Instructor;
import model.Quarter;
import model.Student;
import model.Which92;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class CourseCSVImporter {
	
	private PriorityQueue<Course> courses;
	private HashSet<Instructor> instructors;

	public CourseCSVImporter() {

		courses = new PriorityQueue<Course>();
		instructors = new HashSet<Instructor>();
	}

	public HashSet<Instructor> getInstructors() {

		return new HashSet<Instructor>(instructors);
	}
	
	public PriorityQueue<Course> getCourses() {
		
		return courses;
	}
	
	public void importThings() {
		
		FileDialog fileChooser = new FileDialog(new Shell(), SWT.MULTI);
		fileChooser.setFilterExtensions(new String[] { "*.csv" });
		String file = fileChooser.open();
		String filePath = file.substring(0, file.lastIndexOf("\\") + 1);
		Random random = new Random();
		
		if (file != null) {
			
			String[] files = fileChooser.getFileNames();
			
			for (int i = 0; i < files.length; i++) {
				BufferedReader reader = null;

				try {

					reader = new BufferedReader(new FileReader(filePath +  files[i]));

				} catch (Exception ex) {
					System.out.println(filePath + files[i] + " could not be opened.");
				}

				String line = "";
				
				try {
					
					reader.readLine();
					
				} catch (IOException e) {

				}

				try {
					
					while ((line = reader.readLine()) != null) {
						
						int cells = new StringTokenizer(line, ",").countTokens();
						String courseNumber = "";
						String section = "";
						String title = "";
						String time = "";
						String days = "";
						String room = "";
						String lastName = "";
						String firstName = "";

						StringTokenizer strTok = new StringTokenizer(line, ",");
						strTok.nextToken();
						strTok.nextToken();
						strTok.nextToken();
						strTok.nextToken();
						strTok.nextToken();
						strTok.nextToken();
						
						courseNumber = strTok.nextToken();
						section = strTok.nextToken();
						title = strTok.nextToken();
						lastName = strTok.nextToken();
						firstName = strTok.nextToken();
						
						strTok.nextToken();
						strTok.nextToken();
						strTok.nextToken();
						strTok.nextToken();
						strTok.nextToken();
						
						days = strTok.nextToken();
						
						strTok.nextToken();
						
						time = strTok.nextToken();
						room = strTok.nextToken();
						room = room.substring(room.lastIndexOf("E")+1, room.length());
						
						try {

							int roomN = Integer.parseInt(room);
							
							if (roomN == 203 || roomN == 204 || roomN == 207) {
								
								int courseN = Integer.parseInt(courseNumber);
								int sec = Integer.parseInt(section);
								int hour = Integer.parseInt(new StringTokenizer(time, ":").nextToken());
								
								StringTokenizer st = new StringTokenizer(lastName, " ");
								lastName = st.nextToken();
								lastName = lastName.replaceAll("\"", "");
								firstName = firstName.replaceAll("\"", "");
								String middleInitial = " ";
								if (st.hasMoreTokens())
									middleInitial = st.nextToken();
								Instructor instructor = new Instructor(firstName, lastName, middleInitial.charAt(0));
								instructor.setColor(new Color(Display.getCurrent(), new RGB(random.nextInt(255), random.nextInt(255), random.nextInt(255))));
								instructors.add(instructor);
								
								Course course = new Course(instructor, sec, courseN, roomN);
								strTok = new StringTokenizer(days, " ");
								HashMap<DayOfWeek, Integer> times = new HashMap<DayOfWeek, Integer>();
								
								while (strTok.hasMoreTokens()) {
									
									days = strTok.nextToken();
									DayOfWeek day;
									if (days.equals("M"))
										day = DayOfWeek.MONDAY;
									else if (days.equals("T"))
										day = DayOfWeek.TUESDAY;
									else if (days.equals("W"))
										day = DayOfWeek.WEDNESDAY;
									else if (days.equals("TH"))
										day = DayOfWeek.THURSDAY;
									else if (days.equals("F"))
										day = DayOfWeek.FRIDAY;
									else
										day = DayOfWeek.SUNDAY;
									
									times.put(day, hour);
								}
								
								course.setTimeOffered(times);
								course.setTitle(title);
								courses.add(course);
							}
							
						} catch (NumberFormatException e) {
							
						}
						
					}
					
				} catch (IOException e) {
					
				}
			}
		}
	}
}


