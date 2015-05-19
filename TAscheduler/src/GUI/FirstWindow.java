package GUI;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.jface.action.Action;

import scheduler.Scheduler;

import org.eclipse.swt.widgets.Button;

import GUI.Instructors.InstructorWindow;
import GUI.schedule.*;

import GUI.student.StudentWindow;
import org.eclipse.jface.action.Separator;

public class FirstWindow extends ApplicationWindow {

	private StudentWindow studentWindow;
	private Scheduler scheduler;
	private Action exitAction;
	private Action saveAction;
	private Action openAction;
	private Action newAction;
	private FileDialog fileChooser;
	private FirstWindow shell;
	private ColorDialog colorChooser;
	private Action nearQColorAction;
	private Action twoQColorAction;
	private Action threeQColorAction;
	private Action paidColorAction;
	private Composite studentComposite;
	private Composite classComposite;
	private Action detachStudentAction;
	private Action detachCourseAction;
	private CTabFolder tabFolder;
	private CTabItem studentTab;
	private CTabItem courseTab;
	
	/**
	 * @wbp.parser.constructor 
	 */
	public FirstWindow() {
		super(null);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
	}
	
	public FirstWindow(Scheduler s) {
		super(null);
		scheduler = s;
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
		
		shell = this;
		
		if (scheduler == null) 
			scheduler = new Scheduler();
		
		final Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout());

		tabFolder = new CTabFolder(container, SWT.NONE);
		tabFolder.setBounds(0, 0, 784, 24);
		tabFolder.setSimple(false);

		studentTab = new CTabItem(tabFolder, SWT.NONE);
		studentTab.setText("Students");

		studentComposite = new Composite(tabFolder, SWT.NONE);
		studentTab.setControl(studentComposite);
		studentComposite.setLayout(new FillLayout());

		studentWindow = new StudentWindow(studentComposite, scheduler);

		courseTab = new CTabItem(tabFolder, SWT.NONE);
		courseTab.setText("Courses and Instructors");
		
		classComposite = new InstructorWindow(tabFolder, SWT.NONE, scheduler);
		courseTab.setControl(classComposite);

		CTabItem scheduleTab = new CTabItem(tabFolder, SWT.NONE);
		scheduleTab.setText("Schedule");
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(tabFolder, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		scheduleTab.setControl(scrolledComposite);

	
		schedule sch = new schedule(scrolledComposite);
		
		
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
		
		{
			
			saveAction = new Action("Save") {
				public void run() {
					
					fileChooser = new FileDialog(shell.getShell(), SWT.SAVE);
					fileChooser.setOverwrite(true);
					String file = fileChooser.open();
					
					try {
						
						ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(file)); 
						writer.writeObject(scheduler);
						writer.close();
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			};
			saveAction.setAccelerator(SWT.CTRL | 'S');
		}
		
		{
			openAction = new Action("Open") {
				public void run() {
					
					fileChooser = new FileDialog(shell.getShell(), SWT.OPEN);
					String file = fileChooser.open();
					
					try {
						
						ObjectInputStream reader = new ObjectInputStream(new FileInputStream(file)); 
						scheduler = (Scheduler)reader.readObject();
						reader.close();
						
						shell.getShell().setVisible(false);
						
						shell = new FirstWindow(scheduler);
						shell.open();
						
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			openAction.setAccelerator(SWT.CTRL | 'O');
		}
		
		{
			
			newAction = new Action("New Workbook") {
				public void run() {
					
					shell.getShell().setVisible(false);
					
					shell = new FirstWindow(scheduler);
					shell.open();
				}
			};
			newAction.setAccelerator(SWT.CTRL | 'N');
		}
		
		{
			nearQColorAction = new Action("Nearest Quarter Color") {				public void run() {
					
					colorChooser = new ColorDialog(Display.getCurrent().getActiveShell());
					colorChooser.setRGB(studentWindow.getNearestQuarterColor().getRGB());
					colorChooser.setText("Nearest Graduation");
					studentWindow.setNearestQuarterColor(new Color(Display.getCurrent(), colorChooser.open()));
					resetStudentComposite(tabFolder);
					studentTab.setControl(studentComposite);
					studentWindow.redraw(studentComposite);
				}
			};
		}
		{
			twoQColorAction = new Action("Two Quarters Away Color") {				public void run() {
					
					colorChooser = new ColorDialog(Display.getCurrent().getActiveShell());
					colorChooser.setRGB(studentWindow.getNextQuarterColor().getRGB());
					colorChooser.setText("Next Quarter Graduation");
					studentWindow.setNextQuarterColor(new Color(Display.getCurrent(), colorChooser.open()));
					resetStudentComposite(tabFolder);
					studentTab.setControl(studentComposite);
					studentWindow.redraw(studentComposite);
				}
			};
		}
		{
			threeQColorAction = new Action("Three Quarters Away Color") {				public void run() {
					colorChooser = new ColorDialog(Display.getCurrent().getActiveShell());
					colorChooser.setRGB(studentWindow.getThreeQuarterColor().getRGB());
					colorChooser.setText("Three Quarters to Graduation");
					studentWindow.setThreeQuarterColor(new Color(Display.getCurrent(), colorChooser.open()));
					resetStudentComposite(tabFolder);
					studentTab.setControl(studentComposite);
					studentWindow.redraw(studentComposite);
				}
			};
		}
		{
			paidColorAction = new Action("Paid Color") {				public void run() {
					colorChooser = new ColorDialog(Display.getCurrent().getActiveShell());
					colorChooser.setRGB(studentWindow.getPaidColor().getRGB());
					colorChooser.setText("Paid Color");
					studentWindow.setPaidColor(new Color(Display.getCurrent(), colorChooser.open()));
					resetStudentComposite(tabFolder);
					studentTab.setControl(studentComposite);
					studentWindow.redraw(studentComposite);
				}
			};
		}
		{
			detachStudentAction = new Action("Detach") {				public void run() {

					final Shell studentShell = new Shell();
					studentShell.setLayout(new FillLayout());
					studentShell.setBounds(0, 0, Display.getCurrent().getMonitors()[0].getBounds().width-10, Display.getCurrent().getBounds().height-50);
					studentTab.dispose();
					resetStudentComposite(studentShell);
					studentWindow.redraw(studentComposite);
					
					studentShell.setText("Student Window");
					studentShell.addShellListener(new ShellListener() {
						public void shellActivated(ShellEvent e) {
							
						}
						
						public void shellIconified(ShellEvent e) {
							
						}
						
						public void shellDeiconified(ShellEvent e) {
							
						}
						
						public void shellClosed(ShellEvent e) {
							studentTab = new CTabItem(tabFolder, SWT.NONE, 0);
							studentTab.setText("Students");

							studentComposite = new Composite(tabFolder, SWT.NONE);
							studentTab.setControl(studentComposite);
							studentComposite.setLayout(new FillLayout());

							studentWindow.redraw(studentComposite);
							tabFolder.setSelection(studentTab);
						}
						
						public void shellDeactivated(ShellEvent e) {
							
						}
					});
					
					studentShell.open();
					MenuManager menu = new MenuManager("Menu");
					MenuManager fileMenu = new MenuManager("File");
					menu.add(fileMenu);
					Action reattachAction = new Action("Reattach") {
						public void run() {
							studentShell.close();
						}
					};
					reattachAction.setAccelerator(SWT.CTRL | 'R');
					fileMenu.add(reattachAction);
					
					studentShell.setMenuBar(menu.createMenuBar(studentShell));
				}
			};
			detachStudentAction.setAccelerator(SWT.CTRL | 'D');
		}
		{
			detachCourseAction = new Action("Detach") {
				public void run() {

					final Shell courseShell = new Shell();
					courseShell.setLayout(new FillLayout());
					courseShell.setBounds(0, 0, Display.getCurrent().getMonitors()[0].getBounds().width-10, Display.getCurrent().getBounds().height-50);
					courseTab.dispose();
					classComposite.dispose();
					classComposite = new InstructorWindow(courseShell, SWT.NONE, scheduler);
					
					courseShell.setText("Course Window");
					courseShell.addShellListener(new ShellListener() {
						public void shellActivated(ShellEvent e) {
							
						}
						
						public void shellIconified(ShellEvent e) {
							
						}
						
						public void shellDeiconified(ShellEvent e) {
							
						}
						
						public void shellClosed(ShellEvent e) {
							courseTab = new CTabItem(tabFolder, SWT.NONE, 1);
							courseTab.setText("Courses and Instructors");

							classComposite = new InstructorWindow(tabFolder, SWT.NONE, scheduler);
							courseTab.setControl(classComposite);

							tabFolder.setSelection(studentTab);
						}
						
						public void shellDeactivated(ShellEvent e) {
							
						}
					});
					
					courseShell.open();
					MenuManager menu = new MenuManager("Menu");
					MenuManager fileMenu = new MenuManager("File");
					menu.add(fileMenu);
					Action reattachAction = new Action("Reattach") {
						public void run() {
							courseShell.close();
						}
					};
					reattachAction.setAccelerator(SWT.SHIFT | 'R');
					fileMenu.add(reattachAction);
					
					courseShell.setMenuBar(menu.createMenuBar(courseShell));
				}
			};
			detachCourseAction.setAccelerator(SWT.SHIFT | 'D');
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
		fileMenu.add(saveAction);
		fileMenu.add(openAction);
		fileMenu.add(newAction);
		fileMenu.add(new Separator());
		fileMenu.add(exitAction);
		
		MenuManager studentMenu = new MenuManager("Students");
		menuManager.add(studentMenu);
		studentMenu.add(detachStudentAction);
		
		MenuManager courseMenu = new MenuManager("Courses/Instructors");
		menuManager.add(courseMenu);
		courseMenu.add(detachCourseAction);
		
		MenuManager settingsMenu = new MenuManager("Settings");
		MenuManager studentSettings = new MenuManager("Students");
		settingsMenu.add(studentSettings);
		MenuManager colorSettingsMenu = new MenuManager("Colors");
		
		studentSettings.add(colorSettingsMenu);
		colorSettingsMenu.add(nearQColorAction);
		colorSettingsMenu.add(twoQColorAction);
		colorSettingsMenu.add(threeQColorAction);
		colorSettingsMenu.add(paidColorAction);
		menuManager.add(settingsMenu);

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
		newShell.addShellListener(new ShellListener() {
			public void shellActivated(ShellEvent e) {
				
			}
			
			public void shellIconified(ShellEvent e) {
				
			}
			
			public void shellDeiconified(ShellEvent e) {
				
			}
			
			public void shellClosed(ShellEvent e) {
				System.exit(0);
			}
			
			public void shellDeactivated(ShellEvent e) {
				
			}
		});
	}
	
	private void resetStudentComposite(Composite control) {
		
		studentComposite.setVisible(false);
		studentComposite.dispose();
		studentComposite = new Composite(control, SWT.NONE);
		studentComposite.setLayout(new FillLayout());
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(Display.getCurrent().getBounds().width, Display.getCurrent().getBounds().height);
	}


}


