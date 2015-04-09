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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
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

public class Window extends ApplicationWindow {
	private Table studentTable;
	private Table availabilityTable;
	private static Map<TableItem, Student> tableMap;
	private static Scheduler scheduler;
	
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
		Composite container = new Composite(parent, SWT.NONE);
		{
			TabFolder tabFolder = new TabFolder(container, SWT.NONE);
			tabFolder.setBounds(0, 0, parent.getBounds().width, 22);
			{
				TabItem studentTab = new TabItem(tabFolder, SWT.NONE);
				studentTab.setText("                         Students                          ");
			}
			{
				TabItem instructorTab = new TabItem(tabFolder, SWT.NONE);
				instructorTab.setText("                         Instructors                         ");

			}
			{
				TabItem courseTab = new TabItem(tabFolder, SWT.NONE);
				courseTab.setText("                         Courses                         ");
			}
		}
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setBounds(0, 24, 944, 583);
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setBounds(0, 0, 425, 583);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		studentTable = new Table(scrolledComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.NO_SCROLL);
		studentTable.setHeaderVisible(true);
		studentTable.setLinesVisible(true);
		
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
							items[j].setBackground(i+1, new Color(Display.getCurrent(), 0, 0, 0));
						
						else
							items[j].setBackground(i+1, new Color(Display.getCurrent(), 255, 255, 255));

					}
				}
				
			}
		});
		
		PriorityQueue<Student> studs = new PriorityQueue<Student>(scheduler.getStudents());
		for (int i = 0; i < studs.size(); i++) {
			
			TableItem item = new TableItem(studentTable, SWT.NONE);

		}
		
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
		
		Listener idSortListener = new Listener() {
			public void handleEvent(Event e) {
				
				Comparator<TableItem> comparataur;
				if ((TableColumn)e.widget == lastColumn)
					comparataur = studentLastNameComparator;
				
				else if (((TableColumn)e.widget) == firstColumn)
					comparataur = studentFirstNameComparator;
				
				else if (((TableColumn)e.widget) == idColumn)
					comparataur = studentIDComparator;
				
				else
					comparataur = studentGraduationComparator;
					
				PriorityQueue<TableItem> sortItems = new PriorityQueue<TableItem>(comparataur);
				sortItems.addAll(tableMap.keySet());
				studentTable.removeAll();
				for (int i = 0; i < sortItems.size(); i++) {
					TableItem item = new TableItem(studentTable, SWT.NONE);
					item.setText(sortItems.remove().getText());
				}
				
				studentTable.redraw();
			}
		};
		lastColumn.addListener(SWT.Selection, idSortListener);
		firstColumn.addListener(SWT.Selection, idSortListener);
		idColumn.addListener(SWT.Selection, idSortListener);
		gradColumn.addListener(SWT.Selection, idSortListener);
		
		scrolledComposite.setContent(studentTable);
		scrolledComposite.setMinSize(studentTable.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		TableItem[] items = studentTable.getItems();
		int nearestYear = studs.peek().getGradYear(); 
		Quarter nearestQuarter = studs.peek().getGradQuarter();
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
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setBounds(423, 0, 521, 244);
		
		availabilityTable = new Table(composite_1, SWT.HIDE_SELECTION | SWT.NO_SCROLL);
		availabilityTable.setBounds(10, 27, 501, 217);
		availabilityTable.setHeaderVisible(true);
		availabilityTable.setLinesVisible(true);
	
		DayOfWeek[] days = DayOfWeek.values();
		
		for (int i = 0; i < 10; i++) {

			TableItem item = new TableItem(availabilityTable, SWT.NONE);

		}
		
		items = availabilityTable.getItems();
		
		TableColumn timeColumn = new TableColumn(availabilityTable, SWT.NONE);
		timeColumn.setWidth(83);
		
		for (int i = 0; i < 10; i++) {
			items[i].setText(0, i+8 + ":00");
		}
		
		for (int i = 0; i < 5; i++) {

			TableColumn column = new TableColumn(availabilityTable, SWT.NONE);
			column.setText(days[i].toString());
			column.setWidth(83);

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
	 * Create the toolbar manager.
	 * @return the toolbar manager
	 */
	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		return toolBarManager;
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
		return new Point(450, 300);
	}
	
	public final Comparator<TableItem> studentLastNameComparator  = new Comparator<TableItem>() {
		
		public int compare(TableItem s1, TableItem s2) {
			return s2.getText(0).compareTo(s2.getText(0));
		}
	};
	
	public final Comparator<TableItem> studentFirstNameComparator  = new Comparator<TableItem>() {
		
		public int compare(TableItem s1, TableItem s2) {
			return s1.getText(1).compareTo(s2.getText(1));
		}
	};
	
	public final Comparator<TableItem> studentIDComparator  = new Comparator<TableItem>() {
		
		public int compare(TableItem s1, TableItem s2) {
			return s1.getText(2).compareTo(s2.getText(2));
		}
	};
	
	public final Comparator<TableItem> studentGraduationComparator  = new Comparator<TableItem>() {
		
		public int compare(TableItem s1, TableItem s2) {
			return s1.getText(3).compareTo(s2.getText(3));
		}
	};
	
}


