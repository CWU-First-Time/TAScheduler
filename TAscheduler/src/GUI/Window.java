package GUI;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.TreeMap;

import objects.Course;
import objects.Instructor;
import objects.Quarter;
import objects.Student;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import scheduler.Scheduler;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.FillLayout;

public class Window extends ApplicationWindow {
	private Table studentTable;
	private Table availabilityTable;
	private static Map<TableItem, Student> tableMap;
	private static Scheduler scheduler;
	private Text nameInput;
	private Text lastNameInput;
	private Text idInput;
	private Text emailInput;
	private Text yearInput;
	private Table table;
	
	public static void main(String[] args) {
		
		scheduler = new Scheduler();
		tableMap = new HashMap<TableItem, Student>();
			
		try {
			Window window = new Window();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * Create the application window.
	 */
	public Window() {
		super(null);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		
		//Main Container
		Composite container = new Composite(parent, SWT.NONE);
		
		//Tab folder for composites
		TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		tabFolder.setBounds(0, 0, 944, 632);
		
		//Tab items
		TabItem tbtmSchedule = new TabItem(tabFolder, SWT.NONE);
		tbtmSchedule.setText("Schedule");
		TabItem studentTab = new TabItem(tabFolder, SWT.NONE);
		studentTab.setText("Student");
		
		//Composites that go in the tabs
		Composite scheduleComposite = new Composite(tabFolder, SWT.NONE);
		tbtmSchedule.setControl(scheduleComposite);
		Composite composite = new Composite(tabFolder, SWT.NONE);
		studentTab.setControl(composite);
		composite.setBounds(0, 24, 944, 583);
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setBounds(0, 0, 425, 583);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setBounds(423, 0, 521, 244);
		
		studentTable = new Table(scrolledComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.NO_SCROLL);
		studentTable.setHeaderVisible(true);
		studentTable.setLinesVisible(true);
		
		TableItem[] items = studentTable.getItems();
		
		availabilityTable = new Table(composite_1, SWT.HIDE_SELECTION | SWT.NO_SCROLL);
		availabilityTable.setBounds(10, 27, 501, 217);
		availabilityTable.setHeaderVisible(true);
		availabilityTable.setLinesVisible(true);
		
		
		studentTable.addSelectionListener(new SelectionListener() {
			
		public void widgetDefaultSelected(SelectionEvent e) {
				
		}
			
		public void widgetSelected(SelectionEvent e) {
				
			
			TableItem selected = studentTable.getSelection()[0];
			Student student = tableMap.get(selected);
			
			DayOfWeek[] days = DayOfWeek.values();
			TableItem[] items = availabilityTable.getItems();
			
			for (int i = 0; i < 5; i++) {
				ArrayList<Integer> times = student.getHoursAvailable().get(days[i]);
				for (int j = 0; j < 10; j++) {
					if (times.contains(j+8)) 
						items[j].setText(i+1, "YES");
					
					else
						items[j].setText(i+1, "");
	
					}
			}
				
			}
		});
		
		
		PriorityQueue<Student> studs = new PriorityQueue<Student>(scheduler.getStudents());
		for (int i = 0; i < studs.size(); i++) {
			
			TableItem item = new TableItem(studentTable, SWT.NONE);

		}
		int nearestYear = studs.peek().getGradYear(); 
		Quarter nearestQuarter = studs.peek().getGradQuarter();
	
		
		
		table = new Table(scheduleComposite, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setBounds(10, 10, 605, 576);
		table.setLinesVisible(true);
		
		TableColumn tblclmnQuarter = new TableColumn(table, SWT.NONE);
		tblclmnQuarter.setText(scheduler.getStudents().peek().getGradQuarter().toString() + scheduler.getStudents().peek().getGradYear());
		tblclmnQuarter.setWidth(100);
		
		TableColumn tblclmnMonday = new TableColumn(table, SWT.NONE);
		tblclmnMonday.setWidth(100);
		tblclmnMonday.setText("Monday");
		
		TableColumn tblclmnTuesday = new TableColumn(table, SWT.NONE);
		tblclmnTuesday.setWidth(100);
		tblclmnTuesday.setText("Tuesday");
		
		TableColumn tblclmnWednesday = new TableColumn(table, SWT.NONE);
		tblclmnWednesday.setWidth(100);
		tblclmnWednesday.setText("Wednesday");
		
		TableColumn tblclmnThursday = new TableColumn(table, SWT.NONE);
		tblclmnThursday.setWidth(100);
		tblclmnThursday.setText("Thursday");
		
		TableColumn tblclmnFriday = new TableColumn(table, SWT.NONE);
		tblclmnFriday.setWidth(100);
		tblclmnFriday.setText("Friday");
		
		
	
		TableColumn tblclmnStudentId = new TableColumn(studentTable, SWT.NONE);
		tblclmnStudentId.setWidth(100);
		tblclmnStudentId.setText("Last Name");
		
		TableColumn tblclmnStudentId_1 = new TableColumn(studentTable, SWT.NONE);
		tblclmnStudentId_1.setWidth(100);
		tblclmnStudentId_1.setText("First Name");
		
		TableColumn tblclmnStudentId_2 = new TableColumn(studentTable, SWT.NONE);
		tblclmnStudentId_2.setWidth(100);
		tblclmnStudentId_2.setText("Student ID");
		
		TableColumn tblclmnGraduation = new TableColumn(studentTable, SWT.NONE);
		tblclmnGraduation.setWidth(100);
		tblclmnGraduation.setText("Graduation");
		scrolledComposite.setContent(studentTable);
		scrolledComposite.setMinSize(studentTable.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
	
		
		
		DayOfWeek[] days = DayOfWeek.values();
		
		for (int i = 0; i < 10; i++) {

			TableItem item = new TableItem(availabilityTable, SWT.NONE);

		}
		
		
		items = availabilityTable.getItems();
		
		
		for (int i = 0; i < items.length; i++) {
					
					Student stud = studs.remove();
					
			String[] info = {stud.getLastName(), stud.getFirstName(), String.valueOf(stud.getStudentID()), stud.getGradQuarter() + " " + stud.getGradYear()};
			items[i].setText(info);
			tableMap.put(items[i], stud);
			
			if (stud.getGradQuarter().equals(nearestQuarter) && stud.getGradYear() == nearestYear)
				items[i].setBackground(new Color(Display.getCurrent(), 200, 100, 150));
			
			else if ((!nearestQuarter.equals(Quarter.FALL) && (stud.getGradYear() == nearestYear && stud.getGradQuarter().getValue() == nearestQuarter.getValue() + 1)) ||
					nearestQuarter.equals(Quarter.FALL) && (stud.getGradQuarter() == Quarter.WINTER && stud.getGradYear() == nearestYear + 1))
				items[i].setBackground(new Color(Display.getCurrent(), 100, 200, 255));
		
		}
	
		
		
		TableColumn timeColumn = new TableColumn(availabilityTable, SWT.NONE);
		timeColumn.setWidth(83);
		
		Label lblName = new Label(composite, SWT.NONE);
		lblName.setAlignment(SWT.RIGHT);
		lblName.setBounds(480, 283, 55, 15);
		lblName.setText("Name:");
		
		nameInput = new Text(composite, SWT.BORDER);
		nameInput.setBounds(541, 280, 170, 21);
		
		lastNameInput = new Text(composite, SWT.BORDER);
		lastNameInput.setBounds(541, 307, 170, 21);
		
		Label lblLastName = new Label(composite, SWT.NONE);
		lblLastName.setAlignment(SWT.RIGHT);
		lblLastName.setBounds(452, 310, 83, 15);
		lblLastName.setText("Last Name:");
		
		idInput = new Text(composite, SWT.BORDER);
		idInput.setBounds(541, 334, 170, 21);
		
		Label lblId = new Label(composite, SWT.NONE);
		lblId.setAlignment(SWT.RIGHT);
		lblId.setBounds(480, 337, 55, 15);
		lblId.setText("Id:");
		
		emailInput = new Text(composite, SWT.BORDER);
		emailInput.setBounds(541, 361, 170, 21);
		
		Combo quarterComboSelect = new Combo(composite, SWT.NONE);
		quarterComboSelect.setItems(new String[] {"F", "W", "S", "Su"});
		quarterComboSelect.setBounds(541, 388, 170, 23);
		quarterComboSelect.setText("Select Quarter");
		
		Label lblEmail = new Label(composite, SWT.NONE);
		lblEmail.setAlignment(SWT.RIGHT);
		lblEmail.setBounds(480, 364, 55, 15);
		lblEmail.setText("E-mail:");
		
		Label lblGraduation = new Label(composite, SWT.NONE);
		lblGraduation.setAlignment(SWT.RIGHT);
		lblGraduation.setBounds(452, 391, 83, 15);
		lblGraduation.setText("Graduation:");
		
		yearInput = new Text(composite, SWT.BORDER);
		yearInput.setBounds(757, 390, 76, 21);
		
		Label lblYear = new Label(composite, SWT.NONE);
		lblYear.setAlignment(SWT.RIGHT);
		lblYear.setBounds(715, 391, 36, 15);
		lblYear.setText("Year:");
		
		Label lblAddStudent = new Label(composite, SWT.NONE);
		lblAddStudent.setAlignment(SWT.CENTER);
		lblAddStudent.setBounds(480, 262, 353, 15);
		lblAddStudent.setText("Add Student");
		
		Button btnAddStudent = new Button(composite, SWT.NONE);
		btnAddStudent.setBounds(628, 435, 123, 25);
		btnAddStudent.setText("Add Student");
		
		for (int i = 0; i < 10; i++) {
			items[i].setText(0, i+8 + ":00");
		}
		
		for (int i = 0; i < 5; i++) {

			TableColumn column = new TableColumn(availabilityTable, SWT.NONE);
			column.setText(days[i].toString());
			column.setWidth(83);
			
			for (int j = 0; j < 10; j++) {
				items[j].setText(i+1, "X");
			}
		}

		return container;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Create the menu manager.
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager("menu");
		{
			MenuManager fileMenu = new MenuManager("File");
			Action exitAction = new Action("Exit") {
				public void run() {
					System.exit(0);
				}
			};
			exitAction.setAccelerator(SWT.ALT | SWT.F4);
			fileMenu.add(new Separator());
			
			fileMenu.add(exitAction);
			fileMenu.setVisible(true);
			menuManager.add(fileMenu);
			
			MenuManager helpMenu = new MenuManager("Help");
			Action helpAction = new Action("Help") {
				public void run() {
					
				}
			};
			helpAction.setAccelerator(SWT.F10);
			helpMenu.add(helpAction);
			menuManager.add(helpMenu);
		}
		return menuManager;
	}

	/**
	 * Create the status line manager.
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("New Application");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(954, 689);
	}
}


