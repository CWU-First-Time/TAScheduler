package GUI.student;

import java.awt.EventQueue;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import model.Course;
import model.Grade;
import model.Quarter;
import model.Student;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import GUI.DisableSortPrompt;
import scheduler.Scheduler;

public class StudentWindow {

	private Table studentTable;
	private Table availabilityTable;
	private Table gradeEntryTable;
	private TableCursor tCursor;
	private Map<TableItem, Student> studentMap;
	private Course[] courseColumns;
	private Scheduler scheduler;
	private Text nameInput;
	private Text lastNameInput;
	private Text idInput;
	private Text emailInput;
	private Text yearInput;
	private Combo quarterComboSelect;
	private TableColumn[] studentTableColumns;
	private Color availabilityColor;
	private Color nearestQuarterColor;
	private Color nextQuarterColor;
	private Color threeQuarterColor;
	private Display display;
	private SashForm sashForm;
	private Composite addStudentComposite;
	private ArrayList<Combo> gradeCombos;
	private Button btnAddStudent;
	private DayOfWeek[] days = DayOfWeek.values();
	
	/**
	 * Create the application.
	 */
	public StudentWindow(Composite parent, Scheduler s) {

		if (s != null) 
			scheduler = s;
		else
			scheduler = new Scheduler();
		
		gradeCombos = new ArrayList<Combo>();
		courseColumns = new Course[scheduler.getCourses().size()];
		studentMap = new HashMap<TableItem, Student>();
		display = Display.getCurrent();
		availabilityColor = display.getSystemColor(SWT.COLOR_GREEN);
		nearestQuarterColor = display.getSystemColor(SWT.COLOR_RED);
		nextQuarterColor = display.getSystemColor(SWT.COLOR_YELLOW);
		threeQuarterColor = new Color(display, 125, 218, 232);


		final Shell shell = parent.getShell();
		
		sashForm = new SashForm(parent, SWT.VERTICAL);
		sashForm.setLocation(0, 0);
		sashForm.setSize(784, 542);


		// Craete student table
		studentTable = new Table(sashForm, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION | SWT.NO_FOCUS | SWT.HIDE_SELECTION);
		studentTable.setHeaderVisible(true);
		studentTable.setLinesVisible(true);
		
		// Listener to update availability table for selected student
		studentTable.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {

			}

			public void widgetSelected(SelectionEvent e) {

				btnAddStudent.setText("New Student");
				TableItem selected = studentTable.getSelection()[0];
				Student student = studentMap.get(selected);

				updateLowerWidgets(student);

			}
		});

		// Populates student table with empty items
		PriorityQueue<Student> studs = new PriorityQueue<Student>(
				scheduler.getStudents());
		for (int i = 0; i < studs.size(); i++) {

			TableItem item = new TableItem(studentTable, SWT.NONE);
			
		}

		// Student List Columns
		final TableColumn lastColumn = new TableColumn(studentTable, SWT.CHECK);
		lastColumn.setWidth(100);
		lastColumn.setText("Last Name");

		final TableColumn firstColumn = new TableColumn(studentTable, SWT.NONE);
		firstColumn.setWidth(100);
		firstColumn.setText("First Name");

		final TableColumn idColumn = new TableColumn(studentTable, SWT.NONE);
		idColumn.setWidth(100);
		idColumn.setText("Student ID");

		final TableColumn gradColumn = new TableColumn(studentTable, SWT.NONE);
		gradColumn.setWidth(100);
		gradColumn.setText("Graduation");
		
		final TableColumn emailColumn = new TableColumn(studentTable, SWT.NONE);
		emailColumn.setWidth(100);
		emailColumn.setText("Email");

		ScrolledComposite composite_1 = new ScrolledComposite(sashForm, SWT.H_SCROLL | SWT.V_SCROLL);
		composite_1.setMinSize(460, 214);

		// Listener for student columns
		final Listener columnSortListener = new Listener() {
			public void handleEvent(Event e) {

				Comparator<TableItem> comparataur = null;
				if ((TableColumn) e.widget == gradColumn)
					comparataur = studentGraduationComparator;

				else {

					int columnIndex = 0;
					for (int i = 0; i < studentTableColumns.length; i++) {

						if (((TableColumn)e.widget).equals(studentTableColumns[i])) {
							columnIndex = i;
							break;
						}
					}
					
					if (scheduler.getStudentColumnsGrayed()[columnIndex])
						return;

					comparataur = new ColumnComparator(columnIndex);
				}

				PriorityQueue<TableItem> sortItems = new PriorityQueue<TableItem>(comparataur);
				sortItems.addAll(studentMap.keySet());

				while (!sortItems.isEmpty()) {
					PriorityQueue<Course> classes = new PriorityQueue<Course>(scheduler.getCourses());
					TableItem item = sortItems.remove();
					String[] text = new String[5];

					for (int j = 0; j < 5; j++)
						text[j] = item.getText(j);

					Student stud = studentMap.remove(item);
					item.dispose();
					item = new TableItem(studentTable, SWT.NONE);
					item.setText(text);

					studentMap.put(item, stud);

					HashMap<Course, Grade> grades = stud.getClassesTaken();

					for (int i = 5; i < studentTableColumns.length; i++) {

						if (grades.containsKey(classes.peek()) && !scheduler.getStudentColumnsGrayed()[i])
							item.setText(i, grades.get(classes.remove()).toString());

					}

					item.setBackground(getStudentColor(stud));

				}
			}
		};
		
		final Listener columnGrayListener = new Listener() {
			public void handleEvent(Event e) {
				
				TableItem[] students = studentTable.getItems();

				int columnIndex = 0;
				for (int i = 0; i < studentTableColumns.length; i++) {

					if ((TableColumn) e.widget == studentTableColumns[i]) {
						columnIndex = i;
						break;
					}
				}
				
				if (scheduler.getStudentColumnsGrayed()[columnIndex]) {
					for (int i = 0; i < students.length; i++) 
						
						students[i].setText(columnIndex, studentMap.get(students[i]).getGrade(courseColumns[columnIndex-5]).toString());
					
					if (studentTable.getSelectionCount() > 0) {
						try {
							gradeCombos.get(columnIndex-5).select(studentMap.get(studentTable.getSelection()[0]).getGrade(courseColumns[columnIndex]).getValue());
							
						} catch (NullPointerException ex) {
							
						}
					
					}
					
					scheduler.getStudentColumnsGrayed()[columnIndex] = false;
					
				} else {

					for (int i = 0; i < students.length; i++) {

						students[i].setText(columnIndex, "");
					}
					
					if (studentTable.getSelectionCount() > 0)
						gradeCombos.get(columnIndex-5).select(Grade.values().length);
					
					scheduler.getStudentColumnsGrayed()[columnIndex] = true;

				}
			}
		};
		
		lastColumn.addListener(SWT.Selection, columnSortListener);
		firstColumn.addListener(SWT.Selection, columnSortListener);
		idColumn.addListener(SWT.Selection, columnSortListener);
		gradColumn.addListener(SWT.Selection, columnSortListener);
		emailColumn.addListener(SWT.Selection, columnSortListener);
		
		// Course columns
		PriorityQueue<Course> classes = new PriorityQueue<Course>(
				scheduler.getCourses());
		while (!classes.isEmpty()) {

			final TableColumn classColumn = new TableColumn(studentTable,
					SWT.NONE);
			classColumn.setWidth(100);
			classColumn.setText("CS " + classes.remove().getCourseNumber());

			classColumn.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					
					DisableSortPrompt prompt = new DisableSortPrompt(display.getActiveShell(), classColumn.getText());
					prompt.open();
					
					while(!prompt.isDisposed()) {
						if (!display.readAndDispatch()) {
							display.sleep();
						}
					}
					
					boolean sort = prompt.getSort();

					if (sort) {
						
						columnSortListener.handleEvent(e);
						
					} else {
						
						columnGrayListener.handleEvent(e);
						
					}
				}
			});

		}
		
		// Array of student items
		TableItem[] items = studentTable.getItems();
		
		studentTableColumns = studentTable.getColumns();
		scheduler.setStudentColumnsGrayed(new boolean[studentTable.getColumnCount()]);

		// Adds students to the table and color codes them if they graduate
		// within three quarters
		for (int i = 0; i < items.length; i++) {

			Student stud = studs.remove();

			String[] info = { stud.getLastName(), stud.getFirstName(),
					String.valueOf(stud.getStudentID()),
					stud.getGradQuarter() + " " + stud.getGradYear(),
					stud.getEmail()};
			items[i].setText(info);
			studentMap.put(items[i], stud);

			HashMap<Course, Grade> grades = stud.getClassesTaken();
			classes = new PriorityQueue<Course>(scheduler.getCourses());
			for (int j = 0; j < studentTableColumns.length; j++) {

				if (grades.containsKey(classes.peek())) {
					System.out.println(scheduler.getStudentColumnsGrayed()[j]);
					if (!scheduler.getStudentColumnsGrayed()[j+5]) 
						items[i].setText(j + 5, grades.get(classes.remove()).toString());
					else
						classes.remove();
				}
			}
			
			items[i].setBackground(getStudentColor(stud));

		}

		final Composite lowerScroll = new Composite(composite_1, SWT.NONE);
		lowerScroll.setBounds(0, 0, Display.getDefault().getMonitors()[0].getBounds().width - 25, 214);
		// Availability table showing available times for each selected student
		availabilityTable = new Table(lowerScroll, SWT.HIDE_SELECTION | SWT.FULL_SELECTION);
		tCursor = new TableCursor(availabilityTable, SWT.NONE);
		tCursor.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				
				TableItem selected = tCursor.getRow();
				int column = tCursor.getColumn();
				
				if (column > 0) {
					if (selected.getBackground(column).equals(availabilityColor)) {
						
						selected.setBackground(column, display.getSystemColor(SWT.COLOR_WHITE));
						selected.setText(column, "");
						
					} else {
						
						selected.setBackground(column, availabilityColor);
						selected.setText(column, "YES");
						
					}
				}
				
				availabilityTable.setSelection(-1);
				tCursor.setSelection(0, 0);
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		availabilityTable.setBounds(0, 0, 460, 214);
		availabilityTable.setHeaderVisible(true);
		availabilityTable.setLinesVisible(true);
		
		// Populates availability table with empty items
		for (int i = 0; i < 10; i++) {

			TableItem item = new TableItem(availabilityTable, SWT.NONE);

		}
		items = availabilityTable.getItems();

		// Adds times for availability table
		for (int i = 0; i < 10; i++) {
			items[i].setText(0, i + 8 + ":00");
		}

		// Columns for availability table
		TableColumn timeColumn = new TableColumn(availabilityTable, SWT.NONE);
		timeColumn.setWidth(45);
		timeColumn.setResizable(false);

		// Populates availability column headers
		for (int i = 0; i < 5; i++) {

			TableColumn column = new TableColumn(availabilityTable, SWT.CENTER);
			column.setText(days[i].toString());
			column.setWidth(83);
			column.setResizable(false);

		}
		lowerScroll.setLayout(new FillLayout());
		addStudentComposite = new Composite(lowerScroll, SWT.NONE);
		composite_1.setContent(lowerScroll);

		Label lblName = new Label(addStudentComposite, SWT.NONE);
		lblName.setAlignment(SWT.RIGHT);
		lblName.setBounds(0, 10, 69, 15);
		lblName.setText("First Name:");

		nameInput = new Text(addStudentComposite, SWT.BORDER);
		nameInput.setBounds(75, 7, 170, 21);

		lastNameInput = new Text(addStudentComposite, SWT.BORDER);
		lastNameInput.setBounds(75, 34, 170, 21);

		Label lblLastName = new Label(addStudentComposite, SWT.NONE);
		lblLastName.setAlignment(SWT.RIGHT);
		lblLastName.setBounds(-13, 37, 83, 15);
		lblLastName.setText("Last Name:");

		idInput = new Text(addStudentComposite, SWT.BORDER);
		idInput.setBounds(75, 61, 170, 21);

		Label lblId = new Label(addStudentComposite, SWT.NONE);
		lblId.setAlignment(SWT.RIGHT);
		lblId.setBounds(10, 64, 55, 15);
		lblId.setText("ID:");

		emailInput = new Text(addStudentComposite, SWT.BORDER);
		emailInput.setBounds(75, 88, 170, 21);

		quarterComboSelect = new Combo(addStudentComposite, SWT.NONE);
		quarterComboSelect.setItems(new String[] { "Winter", "Spring", "Summer",
				"Fall" });
		quarterComboSelect.setBounds(75, 115, 170, 23);
		quarterComboSelect.setText("Select Quarter");

		Label lblEmail = new Label(addStudentComposite, SWT.NONE);
		lblEmail.setAlignment(SWT.RIGHT);
		lblEmail.setBounds(14, 91, 55, 15);
		lblEmail.setText("E-mail:");

		Label lblGraduation = new Label(addStudentComposite, SWT.NONE);
		lblGraduation.setAlignment(SWT.RIGHT);
		lblGraduation.setBounds(-14, 121, 83, 15);
		lblGraduation.setText("Grad Q:");

		yearInput = new Text(addStudentComposite, SWT.BORDER);
		yearInput.setBounds(75, 144, 170, 21);

		Label lblYear = new Label(addStudentComposite, SWT.NONE);
		lblYear.setAlignment(SWT.RIGHT);
		lblYear.setBounds(33, 143, 36, 15);
		lblYear.setText("Year:");

		btnAddStudent = new Button(addStudentComposite, SWT.NONE);
		btnAddStudent.setBounds(0, 175, 123, 25);
		btnAddStudent.setText("Add Student");
		btnAddStudent.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				
				if (studentTable.getSelectionCount() > 0) {
					
					updateLowerWidgets(new Student("", "", 0, Quarter.FALL, 0, ""));
					studentTable.setSelection(-1);
					btnAddStudent.setText("Add Student");
					System.out.println("Selected");
					
				} else {
				
					String firstName = nameInput.getText();
					String lastName = lastNameInput.getText();

					int studentID = 0;
					if (idInput.getText().length() == 8)
						studentID = Integer.parseInt(idInput.getText());
					else
						return;

					String email = "";
					if (emailInput.getText().contains("@")
							&& emailInput.getText().contains(".")
							&& emailInput.getText().indexOf("@") < emailInput
									.getText().indexOf("."))
						email = emailInput.getText();
					else
						return;
						
					int year = Integer.parseInt(yearInput.getText());

					Quarter gradQ = Quarter.values()[quarterComboSelect.getSelectionIndex()];

					Student newStudent = new Student(firstName, lastName, studentID, gradQ, year, email);
					scheduler.addStudent(newStudent);
					
					for (int i = 0; i < courseColumns.length; i++) {
						
						int gradeIndex = gradeCombos.get(i).getSelectionIndex();
						
						if (gradeIndex < Grade.values().length && gradeIndex >= 0)
							newStudent.addCourse(courseColumns[i], Grade.values()[gradeIndex]);
							
					}

					TableItem newItem = new TableItem(studentTable, SWT.NONE, 0);
					studentMap.put(newItem, newStudent);

					String[] info = { lastName, firstName,
							String.valueOf(studentID), gradQ + " " + year,
							email};
					newItem.setText(info);
					newItem.setBackground(getStudentColor(newStudent));
					
					for (int i = 5; i < gradeEntryTable.getColumnCount() + 5; i++) {
						
						if (!scheduler.getStudentColumnsGrayed()[i])
							newItem.setText(i, gradeCombos.get(i-5).getText());
					}
					
					HashMap<DayOfWeek, ArrayList<Integer>> times = new HashMap<DayOfWeek, ArrayList<Integer>>();
					
					TableItem[] items = availabilityTable.getItems();
					for (int i = 0; i < days.length; i++) {
						
						ArrayList<Integer> hours = new ArrayList<Integer>();
						
						for (int j = 0; j < items.length; j++) {
							
							if (items[j].getText(i+1).equals("YES")) 
								hours.add(j+8);
						}
						times.put(days[i], hours);
					}
					
					newStudent.setHoursAvailable(times);
					
					Set<TableItem> students = studentMap.keySet();
					for (TableItem i : students) {
						
						i.setBackground(getStudentColor(studentMap.get(i)));
					}
					
					studentTable.setSelection(0);
					
				}

			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});

		Button updateStudentButton = new Button(addStudentComposite, SWT.NONE);
		updateStudentButton.setBounds(142, 175, 116, 25);
		updateStudentButton.setText("Update");
		updateStudentButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				
				TableItem row = studentTable.getSelection()[0];
				Student selected = studentMap.get(row);
				ArrayList<String> inputs = new ArrayList<String>();
				
				inputs.add(lastNameInput.getText());
				selected.setLastName(lastNameInput.getText());
				
				inputs.add(nameInput.getText());
				selected.setFirstName(nameInput.getText());
				
				inputs.add(idInput.getText());
				selected.setStudentID(Integer.parseInt(idInput.getText()));
				
				inputs.add(quarterComboSelect.getText() + " " + Integer.parseInt(yearInput.getText()));
				selected.setGradYear(Integer.parseInt(yearInput.getText()));
				selected.setGradQuarter(Quarter.values()[quarterComboSelect.getSelectionIndex()]);
				
				inputs.add(emailInput.getText());
				selected.setEmail(emailInput.getText());
				
				HashMap<DayOfWeek, ArrayList<Integer>> times = new HashMap<DayOfWeek, ArrayList<Integer>>();
				for (int i = 1; i < availabilityTable.getColumnCount(); i++) {
					
					ArrayList<Integer> hours = new ArrayList<Integer>();
					for (int j = 0; j < availabilityTable.getItemCount(); j++) {
						if (availabilityTable.getItem(j).getText(i).equals("YES"))
							hours.add(j+8);
					}
					
					times.put(days[i-1], hours);
				}
				
				selected.setHoursAvailable(times);
				
				for (int i = 0; i < gradeEntryTable.getColumnCount(); i++) {
					
					if (!scheduler.getStudentColumnsGrayed()[i+5]) {
						
						inputs.add(gradeCombos.get(i).getText());
						
						int gradeIndex = gradeCombos.get(i).getSelectionIndex();
						if (gradeIndex < Grade.values().length && gradeIndex >= 0)
							selected.getClassesTaken().put(courseColumns[i], Grade.values()[gradeIndex]);
						else
							selected.getClassesTaken().remove(courseColumns[i]);
						
					} else {
						
						inputs.add("");
						gradeCombos.get(i).select(Grade.values().length);
						
					}

				}
				
				String[] rowText = new String[inputs.size()];
				inputs.toArray(rowText);
				row.setText(rowText);
				row.setBackground(getStudentColor(selected));
				
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});

		addStudentComposite.setBounds(availabilityTable.getBounds().width + 5, 0, 260, 214);
		sashForm.setWeights(new int[] { 310, 209 });
		
		gradeEntryTable = new Table(lowerScroll, SWT.HIDE_SELECTION);
		gradeEntryTable.setHeaderVisible(true);
		gradeEntryTable.setLinesVisible(true);
		final TableItem gradeList = new TableItem(gradeEntryTable, SWT.NONE);
		
		classes = new PriorityQueue<Course>(scheduler.getCourses());
		int numClasses = 0;
		
		while (!classes.isEmpty()) {
			
			TableColumn gradeColumn = new TableColumn(gradeEntryTable, SWT.NONE);
			
			courseColumns[numClasses++] = classes.peek();
			gradeColumn.setText("CS " + classes.remove().getCourseNumber());
			gradeColumn.setWidth(75);

		}
		
		for (int i = 0; i < gradeEntryTable.getColumnCount(); i++) {
			
			// Place a combo box in each column
			TableEditor editor = new TableEditor(gradeEntryTable);
			Combo grades = new Combo(gradeEntryTable, SWT.NONE);
			grades.setItems(new String[] { "A", "B", "C",
			"D", "F", "" });
			editor.grabHorizontal = true;
			editor.setEditor(grades, gradeList, i);
			gradeCombos.add(grades);
			
		}

		gradeEntryTable.setBounds(addStudentComposite.getBounds().width + availabilityTable.getBounds().width + 30, 100, lowerScroll.getBounds().width - (addStudentComposite.getBounds().width + availabilityTable.getBounds().width) - 35, 60);
	
		shell.addControlListener(new ControlListener() {
			public void controlResized(ControlEvent e) {

				
				try {

					int bottomHeight = 450;
					int width = shell.getClientArea().width;
					int[] weights = sashForm.getWeights();
					
					double ratio = (double) bottomHeight / (double) width;
					
					weights[1] = (int)(ratio*1000);
					weights[0] = 1000 - weights[1];
					
					sashForm.setWeights(weights);

				} catch(IllegalArgumentException ex) {
					
				}
			}
			
			public void controlMoved(ControlEvent e) {
				
			}
		});
	
	}
	
	private void updateLowerWidgets(Student student) {
		
		// Update availability
		TableItem[] items = availabilityTable.getItems();
		
		for (int i = 0; i < 5; i++) {
			
			ArrayList<Integer> times = student.getHoursAvailable().get(days[i]);
			
			for (int j = 0; j < 10; j++) {
				if (times != null && times.contains(j + 8)) {
					
					items[j].setText(i + 1, "YES");
					items[j].setBackground(i + 1, availabilityColor);

				} else {
					
					items[j].setText(i + 1, "");
					items[j].setBackground(i + 1, display.getSystemColor(SWT.COLOR_WHITE));

				}
			}
		}
		
		// Update grades
		HashMap<Course, Grade> grades = student.getClassesTaken();
		PriorityQueue<Course> classes = new PriorityQueue<Course>(scheduler.getCourses());
		
		for (int j = 0; j < gradeEntryTable.getColumnCount(); j++) {
			
			Course course = classes.remove();
			if (grades.containsKey(course) && !scheduler.getStudentColumnsGrayed()[j+5])
				gradeCombos.get(j).select(grades.get(course).getValue());
			else 
				gradeCombos.get(j).select(Grade.values().length);

		}
		
		// Update texts
		nameInput.setText(student.getFirstName());
		lastNameInput.setText(student.getLastName());
		idInput.setText(String.valueOf(student.getStudentID()));
		emailInput.setText(student.getEmail());
		yearInput.setText(String.valueOf(student.getGradYear()));
		quarterComboSelect.select(student.getGradQuarter().getValue());
		
		if (student.getClassesTaken().isEmpty()) {
			yearInput.setText("");
			quarterComboSelect.setText("Select Quarter");
			idInput.setText("");
		}
		
	}
	
	public Color getStudentColor(Student s) {
		
		final int nearestYear = scheduler.getStudents().peek().getGradYear();
		final Quarter nearestQuarter = scheduler.getStudents().peek().getGradQuarter();
		
		Quarter gradQ = s.getGradQuarter();
		int gradYear = s.getGradYear();
		
		if (gradQ.equals(nearestQuarter) && gradYear == nearestYear)
			return nearestQuarterColor;

		else if ((!nearestQuarter.equals(Quarter.FALL) && (gradYear == nearestYear && gradQ
				.getValue() == nearestQuarter.getValue() + 1))
				|| nearestQuarter.equals(Quarter.FALL)
				&& (gradQ == Quarter.WINTER && gradYear == nearestYear + 1))
			return nextQuarterColor;

		else if ((!nearestQuarter.equals(Quarter.SUMMER)
				&& !nearestQuarter.equals(Quarter.FALL)
				&& gradYear == nearestYear && gradQ.getValue() == nearestQuarter
				.getValue() + 2)
				|| (nearestQuarter.equals(Quarter.SUMMER)
						&& gradYear == nearestYear + 1 && gradQ
							.equals(Quarter.WINTER))
				|| (nearestQuarter.equals(Quarter.FALL)
						&& gradYear == nearestYear + 1 && gradQ == Quarter.SPRING))
			return threeQuarterColor;
		
		else
			return new Color(display, 255, 255, 255);
	}
	
	// Comparators used for sorting the students in the student table
	public final Comparator<TableItem> studentGraduationComparator = new Comparator<TableItem>() {
		@Override
		public int compare(TableItem s1, TableItem s2) {

			if (studentMap.get(s1).getGradYear() - studentMap.get(s2).getGradYear() != 0)
				
				return studentMap.get(s1).getGradYear() - studentMap.get(s2).getGradYear();
				
			else 
				return studentMap.get(s1).getGradQuarter().getValue() - studentMap.get(s2).getGradQuarter().getValue();
		}
	};
	
	private static final class ColumnComparator implements Comparator<TableItem>, Serializable {

		private int index = 0;

		public ColumnComparator(int i) {
			index = i;
		}

		public int compare(TableItem item1, TableItem item2) {

			return item1.getText(index).compareTo(item2.getText(index));
		}
	}

}
