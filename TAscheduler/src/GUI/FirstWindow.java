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
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.jface.action.Action;

import scheduler.Scheduler;
<<<<<<< HEAD
=======

import org.eclipse.swt.widgets.Button;

import GUI.schedule.*;
>>>>>>> bc281ef7d99fbd3cce8c932b230c785f0b09c685
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
		
<<<<<<< HEAD
=======
		
>>>>>>> bc281ef7d99fbd3cce8c932b230c785f0b09c685
		CTabItem scheduleTab = new CTabItem(tabFolder, SWT.NONE);
		scheduleTab.setText("Schedule");
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(tabFolder, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scheduleTab.setControl(scrolledComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		scrolledComposite.setAlwaysShowScrollBars(true);
		
		//scrolledComposite.setMinSize(400, 2000);
		
		
		schedule sch = new schedule(scrolledComposite);
		
		
		
		/*
		//ScrollableComposite
		//ScrolledComposite scrollComp = new ScrolledComposite(tabFolder, SWT.V_SCROLL | SWT.BORDER);
		
		ScrolledComposite scheduleTabComposite = new ScrolledComposite(tabFolder, SWT.None);
		//scheduleTab.setControl(scheduleTabComposite);
		
		schedule sch = new schedule(scheduleTabComposite);
		
		//scrollComp.setContent(scheduleTabComposite);
		//scrollComp.setMinSize(scheduleTabComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		//System.out.println("Height: " + scrollComp.getMinHeight() + "\nWidth: " + scrollComp.getMinWidth());
		
		scheduleTab.setControl(scheduleTabComposite);
		*/
		
		
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
					String file = fileChooser.open();
					file += ".TASK";
					
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
					fileChooser.setFilterExtensions(new String[] {"*.TASK"});
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

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(Display.getCurrent().getBounds().width, Display.getCurrent().getBounds().height);
	}

<<<<<<< HEAD
}
=======
}
>>>>>>> bc281ef7d99fbd3cce8c932b230c785f0b09c685
