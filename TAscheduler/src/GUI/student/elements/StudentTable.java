package GUI.student.elements;

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
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import scheduler.Scheduler;

import GUI.DisableSortPrompt;

public class StudentTable {
	
	private Table studentTable;
	private Table availabilityTable;
	private Table gradeEntryTable;
	private Map<TableItem, Student> tableMap;
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
	private boolean[] columnGrayed;
	private Display display;
	private SashForm sashForm;
	private Composite addStudentComposite;

	public StudentTable(Composite c, Scheduler s) {
		
		if (s != null) 
			scheduler = s;
		else
			scheduler = new Scheduler();
		
		courseColumns = new Course[scheduler.getCourses().size()];
		tableMap = new HashMap<TableItem, Student>();
		display = Display.getCurrent();
		availabilityColor = display.getSystemColor(SWT.COLOR_GREEN);
		nearestQuarterColor = display.getSystemColor(SWT.COLOR_RED);
		nextQuarterColor = display.getSystemColor(SWT.COLOR_YELLOW);
		threeQuarterColor = display.getSystemColor(SWT.COLOR_GREEN);
		
		studentTable = new Table(c, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION | SWT.NO_FOCUS | SWT.HIDE_SELECTION);
		studentTable.setHeaderVisible(true);
		studentTable.setLinesVisible(true);
		
		studentTable.addMouseListener(new MouseListener() {
			public void mouseUp(MouseEvent e) {
				
			}
			
			public void mouseDown(MouseEvent e) {
				
			}
			
			public void mouseDoubleClick(MouseEvent e) {
				
				
			}
		});

		// Listener to update availability table for selected student
		studentTable.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {

			}

			public void widgetSelected(SelectionEvent e) {

				TableItem selected = studentTable.getSelection()[0];
				Student student = tableMap.get(selected);

				DayOfWeek[] days = DayOfWeek.values();
				TableItem[] items = availabilityTable.getItems();

				// Update availability
				for (int i = 0; i < 5; i++) {
					ArrayList<Integer> times = student.getHoursAvailable().get(
							days[i]);
					for (int j = 0; j < 10; j++) {
						if (times.contains(j + 8)) {
							items[j].setText(i + 1, "YES");
							items[j].setBackground(i + 1, availabilityColor);

						} else {
							items[j].setText(i + 1, "");
							items[j].setBackground(i + 1, display.getSystemColor(SWT.COLOR_WHITE));

						}
					}
				}

				// Update text fields in lower right (input)
				nameInput.setText(student.getFirstName());
				lastNameInput.setText(student.getLastName());
				idInput.setText(String.valueOf(student.getStudentID()));
				emailInput.setText(student.getEmail());
				yearInput.setText(String.valueOf(student.getGradYear()));
				quarterComboSelect.select(student.getGradQuarter().getValue());
				
				//updateGradeEntryTable(student);

			}
		});

		// Populates student table with empty items
		PriorityQueue<Student> studs = new PriorityQueue<Student>(scheduler.getStudents());
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
					
					if (columnGrayed[columnIndex])
						return;

					comparataur = new ColumnComparator(columnIndex);
				}

				PriorityQueue<TableItem> sortItems = new PriorityQueue<TableItem>(comparataur);
				sortItems.addAll(tableMap.keySet());

				while (!sortItems.isEmpty()) {
					PriorityQueue<Course> classes = new PriorityQueue<Course>(scheduler.getCourses());
					TableItem item = sortItems.remove();
					String[] text = new String[5];

					for (int j = 0; j < 5; j++)
						text[j] = item.getText(j);

					Student stud = tableMap.remove(item);
					item.dispose();
					item = new TableItem(studentTable, SWT.NONE);
					item.setText(text);

					tableMap.put(item, stud);

					HashMap<Course, Grade> grades = stud.getClassesTaken();

					for (int i = 5; i < studentTableColumns.length; i++) {

						if (grades.containsKey(classes.peek()) && !columnGrayed[i])
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
				
				if (columnGrayed[columnIndex]) {
					for (int i = 0; i < students.length; i++) {

						students[i].setBackground(columnIndex, students[i].getBackground(0));
						students[i].setForeground(columnIndex, display.getSystemColor(SWT.COLOR_BLACK));

					}
					
					columnGrayed[columnIndex] = false;
					
				} else {

					for (int i = 0; i < students.length; i++) {

						students[i].setBackground(columnIndex, display.getSystemColor(SWT.COLOR_WHITE));
						students[i].setForeground(columnIndex, display.getSystemColor(SWT.COLOR_WHITE));
					}
					
					columnGrayed[columnIndex] = true;

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
		columnGrayed = new boolean[studentTableColumns.length];

		// Adds students to the table and color codes them if they graduate
		// within three quarters
		for (int i = 0; i < items.length; i++) {

			Student stud = studs.remove();

			String[] info = { stud.getLastName(), stud.getFirstName(),
					String.valueOf(stud.getStudentID()),
					stud.getGradQuarter() + " " + stud.getGradYear(),
					stud.getEmail()};
			items[i].setText(info);
			tableMap.put(items[i], stud);

			HashMap<Course, Grade> grades = stud.getClassesTaken();
			classes = new PriorityQueue<Course>(scheduler.getCourses());
			for (int j = 0; j < studentTableColumns.length; j++) {

				if (grades.containsKey(classes.peek()))
					items[i].setText(j + 5, grades.get(classes.remove())
							.toString());
			}
			
			items[i].setBackground(getStudentColor(stud));

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
	
	public void addStudent(Student s) {
		TableItem newItem = new TableItem(studentTable, SWT.NONE, 0);
		tableMap.put(newItem, s);

		String[] info = { s.getLastName(), s.getFirstName(),
				String.valueOf(s.getStudentID()), s.getGradQuarter() + " " + s.getGradYear(),
				s.getEmail()};
		newItem.setText(info);
		newItem.setBackground(getStudentColor(s));
		
		for (int i = 5; i < gradeEntryTable.getColumnCount() + 5; i++)
			newItem.setText(i, gradeEntryTable.getItems()[0].getText(i-5));
		
		Set<TableItem> students = tableMap.keySet();
		for (TableItem i : students) {
			
			i.setBackground(getStudentColor(tableMap.get(i)));
		}
	}
	
	// Comparators used for sorting the students in the student table
	public final Comparator<TableItem> studentGraduationComparator = new Comparator<TableItem>() {
		@Override
		public int compare(TableItem s1, TableItem s2) {

			if (tableMap.get(s1).getGradYear() - tableMap.get(s2).getGradYear() != 0)
				
				return tableMap.get(s1).getGradYear() - tableMap.get(s2).getGradYear();
				
			else 
				return tableMap.get(s1).getGradQuarter().getValue() - tableMap.get(s2).getGradQuarter().getValue();
		}
	};
	
	private static final class ColumnComparator implements Comparator<TableItem> {

		private int index = 0;

		public ColumnComparator(int i) {
			index = i;
		}

		public int compare(TableItem item1, TableItem item2) {

			return item1.getText(index).compareTo(item2.getText(index));
		}
	}
}
