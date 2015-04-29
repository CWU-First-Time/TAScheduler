package GUI;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import model.Course;
import model.Grade;
import model.Quarter;
import model.Student;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import scheduler.Scheduler;

import org.eclipse.swt.widgets.Button;

import GUI.course.CourseWindow;
import GUI.student.StudentWindow;
import org.eclipse.jface.action.Action;

public class FirstWindow extends ApplicationWindow {

	private StudentWindow studentWindow;
	private CourseWindow courseWindow;
	private Scheduler scheduler;
	private Action exitAction;
	
	/**
	 * Create the application window.
	 */
	public FirstWindow() {
		super(null);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
	}

	/**
	 * Create contents of the application window.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		
		scheduler = new Scheduler();
		
		final Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout());

		CTabFolder tabFolder = new CTabFolder(container, SWT.NONE);
		tabFolder.setBounds(0, 0, 784, 24);
		tabFolder.setSimple(false);

		CTabItem studentTab = new CTabItem(tabFolder, SWT.NONE);
		studentTab.setText("Students");

		Composite studentComposite = new Composite(tabFolder, SWT.NONE);
		studentTab.setControl(studentComposite);
		studentComposite.setLayout(new FillLayout());

		studentWindow = new StudentWindow(studentComposite, scheduler);

		CTabItem courseTab = new CTabItem(tabFolder, SWT.NONE);
		courseTab.setText("Courses");
		
		Composite classComposite = new Composite(tabFolder, SWT.NONE);
		courseTab.setControl(classComposite);
		
		courseWindow = new CourseWindow(classComposite, scheduler);
		
		CTabItem scheduleTab = new CTabItem(tabFolder, SWT.NONE);
		scheduleTab.setText("Schedule");

		return container;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
		{
			exitAction = new Action("Close") {				public void run() {
					System.exit(0);
				}
			};
			exitAction.setAccelerator(SWT.ALT | SWT.F4);
		}
	}

	/**
	 * Create the menu manager.
	 * 
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager("menu");
		
		MenuManager fileMenu = new MenuManager("File");
		menuManager.add(fileMenu);
		fileMenu.add(exitAction);
		return menuManager;
	}

	/**
	 * Create the toolbar manager.
	 * 
	 * @return the toolbar manager
	 */
	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		return toolBarManager;
	}

	/**
	 * Create the status line manager.
	 * 
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {

		try {
			FirstWindow window = new FirstWindow();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell.
	 * 
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Teacher's Aide Scheduling Kompanion (TASK)");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(Display.getCurrent().getBounds().width, Display.getCurrent().getBounds().height);
	}

}
