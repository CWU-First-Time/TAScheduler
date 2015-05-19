package GUI.student;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import model.Quarter;
import model.Student;
import model.Which92;

public class StudentCSVImporter {

	private ArrayList<Student> students;

	public StudentCSVImporter() {

		students = new ArrayList<Student>();
	}

	public ArrayList<Student> getStudents() {

		return students;
	}
	
	public void importStudents() {
		
		FileDialog fileChooser = new FileDialog(new Shell(), SWT.MULTI);
		fileChooser.setFilterExtensions(new String[] { "*.csv" });
		String file = fileChooser.open();
		String filePath = file.substring(0, file.lastIndexOf("\\") + 1);
		
		if (file != null) {
			
			String[] files = fileChooser.getFileNames();
			
			for (int i = 0; i < files.length; i++) {
				BufferedReader reader = null;

				try {

					reader = new BufferedReader(new FileReader(filePath +  files[i]));

				} catch (Exception ex) {
					System.out.println(filePath + files[i] + " could not be opened.");
				}

				String name = "";
				String sID = "";
				String email = "";
				String appQuarter = "";
				String gradQuarter = "";
				String whichTA = "";
				String times = "";
				String vb = "";
				String preferredCourses = "";
				//String grades = "";
				//String classTaken = "";

				try {

					name = reader.readLine();
					sID = reader.readLine();
					email = reader.readLine();
					appQuarter = reader.readLine();
					gradQuarter = reader.readLine();
					whichTA = reader.readLine();
					times = reader.readLine();
					vb = reader.readLine();
					reader.readLine();
					preferredCourses = reader.readLine();
					//grades = reader.readLine();
					//classTaken = reader.readLine();
					

				} catch (IOException e1) {

				} catch (NullPointerException e1) {

				}

				StringTokenizer strTok = new StringTokenizer(name, ", ");
				strTok.nextToken();
				String firstName = strTok.nextToken();
				String lastName = strTok.nextToken();

				strTok = new StringTokenizer(sID, ",");
				strTok.nextToken();
				sID = strTok.nextToken();

				strTok = new StringTokenizer(email, ",");
				strTok.nextToken();
				email = strTok.nextToken();

				strTok = new StringTokenizer(gradQuarter, ",");
				strTok.nextToken();
				gradQuarter = strTok.nextToken();
				String gradYear = strTok.nextToken();

				strTok = new StringTokenizer(whichTA, ",");
				strTok.nextToken();
				whichTA = strTok.nextToken();

				strTok = new StringTokenizer(times, ",");
				strTok.nextToken();

				HashMap<DayOfWeek, ArrayList<Integer>> timeMap = new HashMap<DayOfWeek, ArrayList<Integer>>();
				DayOfWeek[] days = DayOfWeek.values();

				for (int j = 0; j < 5; j++) {

					ArrayList<Integer> hours = new ArrayList<Integer>();

					for (int k = 0; k < 9; k++) {

						String next = strTok.nextToken();
						if (next.equals("y") || next.equals("p"))
							hours.add(k + 8);
					}

					timeMap.put(days[j], hours);
				}
				
				strTok = new StringTokenizer(vb, ",");
				strTok.nextToken();
				vb = strTok.nextToken();
				boolean vbExp = false;
				if (vb.equals("TRUE") || vb.equals("true"))
					vbExp = true;
				
				strTok = new StringTokenizer(preferredCourses, ",");
				strTok.nextToken();
				preferredCourses = "";
				while (strTok.hasMoreTokens())
					preferredCourses += strTok.nextToken() + "\n";
				preferredCourses = preferredCourses.substring(1, preferredCourses.length()-2);

				Quarter quarter = Quarter.getQuarter(gradQuarter);
				Which92 which = Which92.getWhich92(whichTA);

				Student stud = new Student(firstName, lastName, Integer.parseInt(sID), 
						quarter, Integer.parseInt(gradYear), email, which, vbExp, preferredCourses);
				
				stud.setHoursAvailable(timeMap);
				
				//strTok = new StringTokenizer(grade, ",");
				
				//stud.setClassesTaken(classesTaken);
				students.add(stud);
				
			}
		}
	}

}
