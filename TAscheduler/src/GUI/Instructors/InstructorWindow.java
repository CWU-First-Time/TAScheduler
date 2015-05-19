package GUI.Instructors;

import java.awt.Font;
import java.util.ArrayList;
import java.util.PriorityQueue;

import model.Course;
import model.Instructor;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import scheduler.Scheduler;

import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class InstructorWindow extends Composite {
	private Table instructorClassTable;
	private Text instructorTextField;
	private Text classTextField;
	private Scheduler scheduler;
	private Text timeTextField;
	private CourseCSVImporter csvImport;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public InstructorWindow(Composite parent, int style, Scheduler s) {
		super(parent, SWT.BORDER);
		csvImport = new CourseCSVImporter();
		setLayout(null);
		scheduler = s;
		instructorClassTable = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		instructorClassTable.setFont(SWTResourceManager.getFont("Segoe UI", 13, SWT.NORMAL));
		instructorClassTable.setBounds(21, 29, 1054, 512);
		instructorClassTable.setHeaderVisible(true);
		instructorClassTable.setLinesVisible(true);
		
		TableColumn tblclmnInstructor = new TableColumn(instructorClassTable, SWT.NONE);
		tblclmnInstructor.setWidth(100);
		tblclmnInstructor.setText("Instructor");
		
		PriorityQueue<Course> courses = new PriorityQueue<Course>(scheduler.getCourses());
		while (!courses.isEmpty())
		{
			TableColumn classes = new TableColumn(instructorClassTable, SWT.NONE);
			classes.setWidth(100);
			classes.setText("CS " + courses.peek().getCourseNumber() + ".00" + courses.remove().getSection());
		
		}
		
		
		
		List classList = new List(this, SWT.BORDER | SWT.V_SCROLL);
		classList.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		classList.setBounds(605, 558, 118, 131);
		
		courses = new PriorityQueue<Course>(scheduler.getCourses());
		while (!courses.isEmpty())
		{
			classList.add("CS "+ courses.peek().getCourseNumber() + ".00" + courses.remove().getSection());
		}
		
		Label lblClass = new Label(this, SWT.CENTER);
		lblClass.setBounds(758, 29, 118, 15);
		lblClass.setText("Class");
		
		this.setBounds(Display.getCurrent().getBounds().width,Display.getCurrent().getBounds().height,0,0);
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setBounds(59, 558, 291, 161);
		
		instructorTextField = new Text(composite, SWT.BORDER);
		instructorTextField.setBounds(23, 22, 138, 25);
		
		Button addInstructorBtn = new Button(composite, SWT.NONE);
		addInstructorBtn.setBounds(180, 22, 88, 25);
		addInstructorBtn.setText("Add Instructor");
		
		classTextField = new Text(composite, SWT.BORDER);
		classTextField.setBounds(23, 88, 138, 25);
		
		Button addClassBtn = new Button(composite, SWT.NONE);
		addClassBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		addClassBtn.setBounds(180, 88, 88, 25);
		addClassBtn.setText("Add Class");
		
		Composite composite_1 = new Composite(this, SWT.NONE);
		composite_1.setBounds(781, 558, 249, 161);
		
		Button mCheck = new Button(composite_1, SWT.CHECK);
		mCheck.setBounds(10, 10, 39, 16);
		mCheck.setText("M");
		
		Button tCheck = new Button(composite_1, SWT.CHECK);
		tCheck.setBounds(55, 10, 39, 16);
		tCheck.setText("T");
		
		Button wCheck = new Button(composite_1, SWT.CHECK);
		wCheck.setBounds(100, 10, 39, 16);
		wCheck.setText("W");
		
		Button thCheck = new Button(composite_1, SWT.CHECK);
		thCheck.setBounds(145, 10, 39, 16);
		thCheck.setText("TH");
		
		Button fCheck = new Button(composite_1, SWT.CHECK);
		fCheck.setBounds(200, 10, 39, 16);
		fCheck.setText("F");
		
		timeTextField = new Text(composite_1, SWT.BORDER);
		timeTextField.setBounds(66, 65, 114, 21);
		
		Label classTimeLabel = new Label(composite_1, SWT.NONE);
		classTimeLabel.setBounds(92, 44, 60, 15);
		classTimeLabel.setText("Class Time:");
		
		Button btnAddToSchedule = new Button(composite_1, SWT.NONE);
		btnAddToSchedule.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnAddToSchedule.setBounds(10, 111, 99, 25);
		btnAddToSchedule.setText("Add to Schedule");
		
		Button removeScheduleBtn = new Button(composite_1, SWT.NONE);
		removeScheduleBtn.setBounds(151, 111, 75, 25);
		removeScheduleBtn.setText("Remove");
		
		List instructorList = new List(this, SWT.BORDER);
		instructorList.setBounds(387, 558, 189, 131);
		
		Button removeInstructorBtn = new Button(this, SWT.NONE);
		removeInstructorBtn.setBounds(429, 694, 109, 25);
		removeInstructorBtn.setText("Remove Instructor");
		
		removeInstructorBtn.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				csvImport.importThings();
				
				for (Course course : csvImport.getCourses())
					scheduler.addCourse(course);
				
				for (Instructor instructor : csvImport.getInstructors())
					scheduler.addInstructor(instructor);
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		Button removeClassBtn = new Button(this, SWT.NONE);
		removeClassBtn.setBounds(623, 694, 85, 25);
		removeClassBtn.setText("Remove Class");
		
		new TableItem(instructorClassTable, SWT.NONE).setText("dkjasghdkasghldasgkdhasgklghdasklgjdhsgfksdgdaskj\nghsdghasgdjsafhdsjafhdsafkhsdakgjdhsgdkj\nashgsdbgmnsagsmgbvsgmzxghzhgmzxhgjsdgsg");
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
