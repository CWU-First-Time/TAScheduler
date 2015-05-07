package GUI.student;

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
import model.Which92;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import GUI.DisableSortPrompt;
import scheduler.Scheduler;

public class StudentWindow {

	private final int nonCourseColumnCount = 6;
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
	private Combo whichComboSelect;
	private TableColumn[] studentTableColumns;
	private Color availabilityColor;
	private Color nearestQuarterColor;
	private Color nextQuarterColor;
	private Color threeQuarterColor;
	private Color paidColor;
	private Display display;
	private SashForm sashForm;
	private Composite addStudentComposite;
	private ArrayList<Combo> gradeCombos;
	private Button btnAddStudent;
	private Button removeStudentButton;
	private DayOfWeek[] days = DayOfWeek.values();
	private Composite container;
	
	/**
	 * Create the application.
	 */
	public StudentWindow(Composite parent, Scheduler s) {

		if (s != null) 
			scheduler = s;
		else
			scheduler = new Scheduler();
		
		display = Display.getCurrent();
		availabilityColor = display.getSystemColor(SWT.COLOR_GREEN);
		nearestQuarterColor = display.getSystemColor(SWT.COLOR_RED);
		nextQuarterColor = display.getSystemColor(SWT.COLOR_YELLOW);
		threeQuarterColor = new Color(display, 125, 218, 232);
		paidColor = new Color(display, 237, 128, 212);
		container = parent;
		
		redraw(parent);
	
	}
	
	public void redraw(Composite parent) {
		
		gradeCombos = new ArrayList<Combo>();
		courseColumns = new Course[scheduler.getCourses().size()];
		studentMap = new HashMap<TableItem, Student>();

		final Shell shell = parent.getShell();
		
		sashForm = new SashForm(parent, SWT.VERTICAL);
		sashForm.setLocation(0, 0);
		sashForm.setSize(parent.getBounds().width, parent.getBounds().height);


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
				removeStudentButton.setVisible(true);
				TableItem selected = studentTable.getSelection()[0];
				Student student = studentMap.get(selected);

				updateLowerWidgets(student);

			}
		});

		// Populates student table with empty items
		PriorityQueue<Student> studs = new PriorityQueue<Student>(scheduler.getStudents());
		
		for (int i = 0; i < studs.size(); i++) {

			new TableItem(studentTable, SWT.NONE);
			
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
		
		final TableColumn whichTAColumn = new TableColumn(studentTable, SWT.NONE);
		whichTAColumn.setWidth(100);
		whichTAColumn.setText("392 / 492 / Paid");

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
					String[] text = new String[nonCourseColumnCount];

					for (int j = 0; j < nonCourseColumnCount; j++)
						text[j] = item.getText(j);

					Student stud = studentMap.remove(item);
					item.dispose();
					item = new TableItem(studentTable, SWT.NONE);
					item.setText(text);

					studentMap.put(item, stud);

					HashMap<Course, Grade> grades = stud.getClassesTaken();

					for (int i = nonCourseColumnCount; i < studentTableColumns.length; i++) {

						Course current = classes.remove();
						if (grades.containsKey(current) && !scheduler.getStudentColumnsGrayed()[i])
							item.setText(i, grades.get(current).toString());
						else
							item.setText(i, "");

					}

					item.setBackground(getPriorityColor(stud));

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
						
						students[i].setText(columnIndex, studentMap.get(students[i]).getGrade(courseColumns[columnIndex-nonCourseColumnCount]).toString());
					
					if (studentTable.getSelectionCount() > 0) {
						
						try {
							gradeCombos.get(columnIndex-nonCourseColumnCount).select(studentMap.get(studentTable.getSelection()[0]).getGrade(courseColumns[columnIndex]).getValue());
							
						} catch (NullPointerException ex) {
							
						}
					
					}
					
					scheduler.getStudentColumnsGrayed()[columnIndex] = false;
					
				} else {

					for (int i = 0; i < students.length; i++) {

						students[i].setText(columnIndex, "");
					}
					
					if (studentTable.getSelectionCount() > 0)
						gradeCombos.get(columnIndex-nonCourseColumnCount).select(Grade.values().length);
					
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
		if (scheduler.getStudentColumnsGrayed() == null) {
			scheduler.setStudentColumnsGrayed(new boolean[studentTable.getColumnCount()]);
		}

		// Adds students to the table and color codes them if they graduate
		// within three quarters
		for (int i = 0; i < items.length; i++) {

			Student stud = studs.remove();

			String[] info = { stud.getLastName(), stud.getFirstName(),
					String.valueOf(stud.getStudentID()),
					stud.getGradQuarter() + " " + stud.getGradYear(),
					stud.getEmail(), stud.getWhich92().toString()};
			items[i].setText(info);
			studentMap.put(items[i], stud);

			HashMap<Course, Grade> grades = stud.getClassesTaken();
			classes = new PriorityQueue<Course>(scheduler.getCourses());
			for (int j = 0; j < studentTableColumns.length; j++) {

				if (grades.containsKey(classes.peek())) {

					if (!scheduler.getStudentColumnsGrayed()[j+nonCourseColumnCount]) 
						items[i].setText(j + nonCourseColumnCount, grades.get(classes.remove()).toString());
					else
						classes.remove();
				}
			}
			
			items[i].setBackground(getPriorityColor(stud));

		}

		final Composite lowerScroll = new Composite(composite_1, SWT.NONE);
		lowerScroll.setBounds(0, 0, Display.getDefault().getMonitors()[0].getBounds().width - 25, 239);
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

			new TableItem(availabilityTable, SWT.NONE);

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
		lblName.setBounds(0, 5, 69, 15);
		lblName.setText("First Name:");

		nameInput = new Text(addStudentComposite, SWT.BORDER);
		nameInput.setBounds(75, 7, 170, 21);

		lastNameInput = new Text(addStudentComposite, SWT.BORDER);
		lastNameInput.setBounds(75, 34, 170, 21);

		Label lblLastName = new Label(addStudentComposite, SWT.NONE);
		lblLastName.setAlignment(SWT.RIGHT);
		lblLastName.setBounds(-13, 32, 83, 15);
		lblLastName.setText("Last Name:");

		idInput = new Text(addStudentComposite, SWT.BORDER);
		idInput.setBounds(75, 61, 170, 21);

		Label lblId = new Label(addStudentComposite, SWT.NONE);
		lblId.setAlignment(SWT.RIGHT);
		lblId.setBounds(14, 64, 55, 15);
		lblId.setText("ID:");

		emailInput = new Text(addStudentComposite, SWT.BORDER);
		emailInput.setBounds(75, 88, 170, 21);

		quarterComboSelect = new Combo(addStudentComposite, SWT.READ_ONLY);
		quarterComboSelect.setItems(new String[] { "Winter", "Spring", "Summer",
				"Fall" });
		quarterComboSelect.setBounds(75, 115, 70, 23);

		Label lblEmail = new Label(addStudentComposite, SWT.NONE);
		lblEmail.setAlignment(SWT.RIGHT);
		lblEmail.setBounds(14, 90, 55, 15);
		lblEmail.setText("E-mail:");

		Label lblGraduation = new Label(addStudentComposite, SWT.NONE);
		lblGraduation.setAlignment(SWT.RIGHT);
		lblGraduation.setBounds(-14, 119, 83, 15);
		lblGraduation.setText("Grad Q:");

		yearInput = new Text(addStudentComposite, SWT.BORDER);
		yearInput.setBounds(75, 144, 170, 21);

		Label lblYear = new Label(addStudentComposite, SWT.NONE);
		lblYear.setAlignment(SWT.RIGHT);
		lblYear.setBounds(4, 146, 65, 15);
		lblYear.setText("Grad Year:");
		
		whichComboSelect = new Combo(addStudentComposite, SWT.READ_ONLY);
		whichComboSelect.setItems(new String[] {"392", "492", "Paid"});
		whichComboSelect.setBounds(200, 115, 45, 23);
		//whichComboSelect.setBounds(75, 170, 45, 23);
		
		Label whichLabel = new Label(addStudentComposite, SWT.NONE);
		whichLabel.setAlignment(SWT.RIGHT);
		whichLabel.setBounds(128, 119, 65, 15);
		// whichLabel.setBounds(5, 175, 65, 15);
		// whichLabel.setText("392 / 492");
		whichLabel.setText("?92:");

		btnAddStudent = new Button(addStudentComposite, SWT.NONE);
		btnAddStudent.setBounds(0, 192, 123, 25);
		btnAddStudent.setText("Add Student");
		btnAddStudent.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				
				if (studentTable.getSelectionCount() > 0) {
					
					updateLowerWidgets(new Student("", "", 0, null, 0, "", null));
					studentTable.setSelection(-1);
					btnAddStudent.setText("Add Student");
					removeStudentButton.setVisible(false);
					
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
					Which92 which92 = Which92.values()[whichComboSelect.getSelectionIndex()];

					Student newStudent = new Student(firstName, lastName, studentID, gradQ, year, email, which92);
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
							email, which92.toString()};
					newItem.setText(info);
					newItem.setBackground(getPriorityColor(newStudent));
					
					for (int i = nonCourseColumnCount; i < gradeEntryTable.getColumnCount() + nonCourseColumnCount; i++) {
						
						if (!scheduler.getStudentColumnsGrayed()[i])
							newItem.setText(i, gradeCombos.get(i-nonCourseColumnCount).getText());
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
						
						i.setBackground(getPriorityColor(studentMap.get(i)));
					}
					
					studentTable.setSelection(0);
					
				}

			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});

		Button updateStudentButton = new Button(addStudentComposite, SWT.NONE);
		updateStudentButton.setBounds(142, 167, 116, 25);
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
				
				inputs.add(whichComboSelect.getText());
				selected.setWhich92(Which92.values()[whichComboSelect.getSelectionIndex()]);
				
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
					
					if (!scheduler.getStudentColumnsGrayed()[i+nonCourseColumnCount]) {
						
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
				row.setBackground(getPriorityColor(selected));
				
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});

		addStudentComposite.setBounds(availabilityTable.getBounds().width + 5, 0, 260, 239);
		sashForm.setWeights(new int[] { 310, 225 });
		
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

					int bottomHeight = 475;
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
		
		Button importStudentButton = new Button(addStudentComposite, SWT.NONE);
		importStudentButton.setBounds(0, 167, btnAddStudent.getBounds().width, btnAddStudent.getBounds().height);
		importStudentButton.setText("Import Students");
		importStudentButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
			
			public void widgetSelected(SelectionEvent e) {
				
				StudentImporter importer = new StudentImporter();
				importer.importStudents();
				ArrayList<Student> newStudents = importer.getStudents();
				
				for (int i = 0; i < newStudents.size(); i++) {
					
					Student newStudent = newStudents.get(i);
					if (studentMap.containsValue(newStudent))
						continue;
					
					scheduler.addStudent(newStudent);
					TableItem newItem = new TableItem(studentTable, SWT.NONE, 0);
					studentMap.put(newItem, newStudent);

					String[] info = {newStudent.getLastName(), newStudent.getFirstName(),
							String.valueOf(newStudent.getStudentID()), newStudent.getGradQuarter() + " " + newStudent.getGradYear(),
							newStudent.getEmail(), newStudent.getWhich92().toString()};
					newItem.setText(info);
					newItem.setBackground(getPriorityColor(newStudent));
				}
				
			}
		});
		
		removeStudentButton = new Button(addStudentComposite, SWT.NONE);
		removeStudentButton.setBounds(142, 192, updateStudentButton.getBounds().width, updateStudentButton.getBounds().height);
		removeStudentButton.setText("Remove Student");
		removeStudentButton.setVisible(false);
		removeStudentButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				TableItem item = studentTable.getItem(studentTable.getSelectionIndex());
				Student selected = studentMap.get(item);
				scheduler.removeStudent(selected);
				studentTable.remove(studentTable.getSelectionIndex());
				studentMap.remove(item);
				removeStudentButton.setVisible(false);
				updateLowerWidgets(new Student("", "", 0, null, 0, "", null));
				
				Set<TableItem> students = studentMap.keySet();
				for (TableItem i : students) {
					
					i.setBackground(getPriorityColor(studentMap.get(i)));
				}
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				
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
			if (grades.containsKey(course) && !scheduler.getStudentColumnsGrayed()[j+nonCourseColumnCount])
				gradeCombos.get(j).select(grades.get(course).getValue());
			else 
				gradeCombos.get(j).select(Grade.values().length);

		}
		
		// Update texts
		nameInput.setText(student.getFirstName());
		lastNameInput.setText(student.getLastName());
		
		String id = "";
		if (student.getStudentID() > 0)
			id = String.valueOf(student.getStudentID());
		idInput.setText(id);
		
		emailInput.setText(student.getEmail());
		
		String year = "";
		if (student.getGradYear() > 0)
			year = String.valueOf(student.getGradYear());
		
		yearInput.setText(year);
		
		Quarter q = student.getGradQuarter();
		if (q != null)
			quarterComboSelect.select(q.getValue());
		else
			quarterComboSelect.deselectAll();
		
		Which92 w = student.getWhich92();
		if (w != null)
			whichComboSelect.select(w.getValue());
		else
			whichComboSelect.deselectAll();
		
	}
	
	public Color getPriorityColor(Student s) {
		
		if (s.getWhich92().equals(Which92.PAID))
			return paidColor;
		
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
	
	public Rectangle getBounds() {
		return sashForm.getBounds();
	}

	public Color getNearestQuarterColor() {
		return nearestQuarterColor;
	}

	public void setNearestQuarterColor(Color nearestQuarterColor) {
		this.nearestQuarterColor = nearestQuarterColor;
	}

	public Color getNextQuarterColor() {
		return nextQuarterColor;
	}

	public void setNextQuarterColor(Color nextQuarterColor) {
		this.nextQuarterColor = nextQuarterColor;
	}

	public Color getThreeQuarterColor() {
		return threeQuarterColor;
	}

	public void setThreeQuarterColor(Color threeQuarterColor) {
		this.threeQuarterColor = threeQuarterColor;
	}

	public Color getPaidColor() {
		return paidColor;
	}

	public void setPaidColor(Color paidColor) {
		this.paidColor = paidColor;
	}

}
