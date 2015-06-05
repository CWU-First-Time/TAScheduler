package GUI.schedule;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import model.Course;
import model.Instructor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import scheduler.Scheduler;


public class schedule {
	
	//private Table tableSetup;
	//TableEditor editor;
	private String header[] = {"QuarterYear", "Monday", "Tuesday", "Wednesday",
						"Thursday", "Friday"};
	
	private List<String> contentsList;
	private Text scheduleCells[];
	private ArrayList<Course> courses;
	
	//Total of 150 cells
	final int SCHEDULE_COLUMNS = 6;
	final int SCHEDULE_ROWS =25;
	
	int ROW_SELECTED = 0;
	int COLUMN_SELECTED = 1;
	SashForm sash;
	GridLayout layout;
	Composite comp, comp2;
	ScrolledComposite comps;
	
	private Text instructorField;
	private Button addInstructorBtn;
	private Button addClassBtn;
	private Text classField;
	private Button removeInstructorBtn;
	private Button setColorBtn;
	private org.eclipse.swt.widgets.List classList;
	private Button removeClassBtn;
	private Button importBtn;
	private Table instructorTable;
	
	private Scheduler scheduler;

	/**
	 * Constructor intializes the all the components of the schedule tab
	 * 
	 * @param comps - the composite object into all the components will be placed in
	 */
	public schedule(ScrolledComposite comps, Scheduler s)
	{
		scheduler = s;
		courses = s.getCourses();
		
		if (!s.getStudents().isEmpty())
			header[0] = s.getStudents().getFirst().getGradQuarter().toString() + " " + s.getStudents().getFirst().getGradYear();
		
		this.comps = comps;
		sash = new SashForm(comps, SWT.BORDER|SWT.VERTICAL);
		//GridLayout
		layout = new GridLayout(SCHEDULE_COLUMNS,true);
		
		
		comp = new Composite(sash, SWT.NONE);
		comp2 = new ScrolledComposite(sash,SWT.H_SCROLL | SWT.V_SCROLL);
		
		instructorField = new Text(comp2, SWT.BORDER);
		instructorField.setText("First Name Last Name");
		instructorField.setBounds(488, 93, 118, 25);
		
		addInstructorBtn = new Button(comp2, SWT.NONE);
		addInstructorBtn.setText("Add Instructor");
		addInstructorBtn.setBounds(612, 91, 88, 25);
		
		addClassBtn = new Button(comp2, SWT.NONE);
		addClassBtn.setText("Add Class");
		addClassBtn.setBounds(611, 148, 89, 25);
		
		classField = new Text(comp2, SWT.BORDER);
		classField.setText("CS ###");
		classField.setBounds(488, 148, 117, 25);
		
		removeInstructorBtn = new Button(comp2, SWT.NONE);
		removeInstructorBtn.setText("Remove");
		removeInstructorBtn.setBounds(796, 197, 67, 25);
		
		setColorBtn = new Button(comp2, SWT.NONE);
		setColorBtn.setText("Set Color");
		setColorBtn.setBounds(723, 197, 67, 25);
		
		classList = new org.eclipse.swt.widgets.List(comp2, SWT.BORDER|SWT.V_SCROLL|SWT.H_SCROLL);
		classList.setBounds(893, 10, 118, 181);
		
		removeClassBtn = new Button(comp2, SWT.NONE);
		removeClassBtn.setText("Remove Class");
		removeClassBtn.setBounds(913, 197, 85, 25);
		
		final CCombo timeCombo = new CCombo(comp2, SWT.BORDER);
		timeCombo.setBounds(1174, 57, 67, 21);
		timeCombo.add("8:00");
		timeCombo.add("9:00");
		timeCombo.add("10:00");
		timeCombo.add("11:00");
		timeCombo.add("12:00");
		timeCombo.add("1:00");
		timeCombo.add("2:00");
		timeCombo.add("3:00");
		timeCombo.add("4:00");
		
		final Button mBtn = new Button(comp2, SWT.CHECK);
		mBtn.setBounds(1059, 33, 32, 16);
		mBtn.setText("M");
		
		final Button tBtn = new Button(comp2, SWT.CHECK);
		tBtn.setText("T");
		tBtn.setBounds(1104, 33, 32, 16);
		
		final Button wBtn = new Button(comp2, SWT.CHECK);
		wBtn.setText("W");
		wBtn.setBounds(1149, 33, 32, 16);
		
		final Button thBtn = new Button(comp2, SWT.CHECK);
		thBtn.setText("TH");
		thBtn.setBounds(1192, 33, 32, 16);
		
		final Button fBtn = new Button(comp2, SWT.CHECK);
		fBtn.setText("F");
		fBtn.setBounds(1240, 33, 32, 16);
		
		final CCombo roomCombo = new CCombo(comp2, SWT.BORDER);
		roomCombo.setBounds(1174, 90, 67, 21);
		roomCombo.add("203");
		roomCombo.add("204");
		roomCombo.add("207");
		
		
		
		final Button updateBtn = new Button(comp2, SWT.NONE);
		updateBtn.setBounds(1125, 166, 99, 25);
		updateBtn.setText("Update");
		
		Label timeLbl = new Label(comp2, SWT.NONE);
		timeLbl.setAlignment(SWT.CENTER);
		timeLbl.setBounds(1081, 57, 55, 15);
		timeLbl.setText("Time");
		
		Label roomLbl = new Label(comp2, SWT.NONE);
		roomLbl.setAlignment(SWT.CENTER);
		roomLbl.setBounds(1081, 94, 55, 15);
		roomLbl.setText("Room #");
		
		Label sectionLbl = new Label(comp2, SWT.NONE);
		sectionLbl.setAlignment(SWT.CENTER);
		sectionLbl.setBounds(1081, 129, 55, 15);
		sectionLbl.setText("Section #");
		
		importBtn = new Button(comp2, SWT.NONE);
		importBtn.setText("Import Schedule");
		importBtn.setBounds(549, 33, 93, 25);
		
		instructorTable = new Table(comp2, SWT.BORDER | SWT.FULL_SELECTION);
		instructorTable.setBounds(723, 10, 140, 181);
		instructorTable.setHeaderVisible(false);
		instructorTable.setLinesVisible(false);
		comps.setContent(sash);
		
		
		//set Scrolled Composite attributes
		comps.setAlwaysShowScrollBars(true);
		comps.setExpandVertical(true);
		comps.setExpandHorizontal(true);
		
		
		layout.horizontalSpacing = 0;
		
		//Set the layout to the composite
		comp.setLayout(layout);
		
		//Test button
		/*
		final Button btn = new Button(comp, SWT.PUSH | SWT.BORDER);
		btn.setText("Test");
		btn.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e)
			{
				addStudentToSchedule("Tony", "Tuesday", 9, 207);
			}
			
		});
		*/
		
		setupContentsV2();
		setupSchedule(comp);
		sash.setWeights(new int[] {837, 199});
		
		Listener listener = new Listener(){
			public void handleEvent(Event event)
			{
				String text;
				if (event.widget == addClassBtn)
				{
					text = classField.getText();
					classList.add(text);
				}
				else if(event.widget == addInstructorBtn)
				{
					TableItem item = new TableItem(instructorTable,SWT.NONE);
					item.setText(instructorField.getText());
					
				}
				else if(event.widget == removeClassBtn)
				{
					if(classList.getSelectionIndex() >= 0)	
						classList.remove(classList.getSelectionIndex());
				}
				else if(event.widget == removeInstructorBtn)
				{
					if(instructorTable.getSelectionIndex()>=0)
						instructorTable.remove(instructorTable.getSelectionIndex());
				}
				
				else if(event.widget == importBtn)
				{
					CourseCSVImporter importer = new CourseCSVImporter();
					importer.importThings();
					for (Instructor i : importer.getInstructors()) 
						scheduler.addInstructor(i);
					
					instructorTable.removeAll();
					classList.removeAll();
					
					for (Instructor i : scheduler.getInstructors())
					{
						TableItem item = new TableItem(instructorTable,SWT.NONE);
						item.setText(i.toString());
						item.setBackground(i.getColor());
					}
					
					PriorityQueue<Course> pq = importer.getCourses();
					
					while (!pq.isEmpty()) 
					{
						Course c = pq.remove();
						scheduler.addCourse(c);
						
						if (c.getRoomNumber() == 203 || c.getRoomNumber() == 204 || c.getRoomNumber() == 207)
						{
							classList.add(c.toString());
							
							String updateItem = "CS " + c.getCourseNumber() + "." + String.format("%03d", c.getSection());
							updateItem += " w/ " + c.getInstructor().toString() + " ";
							updateItem += "\n1.\n2.\n3.";
							
							int time = c.getTimeOffered().get(c.getTimeOffered().keySet().iterator().next()) - 8;
							
							int index = 0;
							if(time == 0)
								index = 6;
							else if(time == 1)
								index = 24;
							else if(time == 2)
								index = 42;
							else if(time == 3)
								index = 60;
							else if(time == 4)
								index = 78;
							else if(time == 5)
								index = 96;
							else if(time == 6)
								index = 114;
							else if(time == 7)
								index = 132;
							else if(time == 8)
								index = 150;
							
							if (c.getRoomNumber() == 204)
								index += 6;
							else if (c.getRoomNumber() == 207)
								index += 12;

							if(c.getTimeOffered().containsKey(DayOfWeek.MONDAY))
							{
								Color color = c.getInstructor().getColor();
								scheduleCells[index+1].setText(updateItem);
								scheduleCells[index+1].setBackground(color);
								
								if (color.getRed() + color.getGreen() + color.getBlue() < (255 * 3) / 2)
									scheduleCells[index + 1].setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
							}
							if(c.getTimeOffered().containsKey(DayOfWeek.TUESDAY))
							{
								Color color = c.getInstructor().getColor();
								scheduleCells[index+2].setText(updateItem);
								scheduleCells[index+2].setBackground(color);
								
								if (color.getRed() + color.getGreen() + color.getBlue() < (255 * 3) / 2)
									scheduleCells[index + 2].setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
							}
							
							if(c.getTimeOffered().containsKey(DayOfWeek.WEDNESDAY))
							{
								Color color = c.getInstructor().getColor();
								scheduleCells[index+3].setText(updateItem);
								scheduleCells[index+3].setBackground(color);
								
								if (color.getRed() + color.getGreen() + color.getBlue() < (255 * 3) / 2)
									scheduleCells[index + 3].setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
							}
							if(c.getTimeOffered().containsKey(DayOfWeek.THURSDAY))
							{
								Color color = c.getInstructor().getColor();
								scheduleCells[index+4].setText(updateItem);
								scheduleCells[index+4].setBackground(color);
								
								if (color.getRed() + color.getGreen() + color.getBlue() < (255 * 3) / 2)
									scheduleCells[index + 4].setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
							}
							if(c.getTimeOffered().containsKey(DayOfWeek.FRIDAY))
							{
								Color color = c.getInstructor().getColor();
								scheduleCells[index+5].setText(updateItem);
								scheduleCells[index+5].setBackground(color);
								
								if (color.getRed() + color.getGreen() + color.getBlue() < (255 * 3) / 2)
									scheduleCells[index + 5].setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
							}
							
							comp.layout();
						}
					}

				}
				
				else if(event.widget == setColorBtn)
				{
					
					ColorDialog cd = new ColorDialog(Display.getCurrent().getActiveShell());
					cd.setText("Instructor List Color");
					TableItem item = instructorTable.getItem(instructorTable.getSelectionIndex());
				
					item.setBackground(new Color(Display.getCurrent(),cd.open()));
					instructorTable.deselectAll();
			        
					
				}
				else if(event.widget == updateBtn)
				{
					String updateItem = classList.getItem(classList.getSelectionIndex()).substring(0, classList.getItem(classList.getSelectionIndex()).lastIndexOf(":")) + " w/ ";
					updateItem += instructorTable.getItem(instructorTable.getSelectionIndex()).getText();
					updateItem += "\n1.\n2.\n3.";
					
					int time = timeCombo.getSelectionIndex();
					
					int index = 0;
					if(time == 0)
						index = 6;
					else if(time == 1)
						index = 24;
					else if(time == 2)
						index = 42;
					else if(time == 3)
						index = 60;
					else if(time == 4)
						index = 78;
					else if(time == 5)
						index = 96;
					else if(time == 6)
						index = 114;
					else if(time == 7)
						index = 132;
					else if(time == 8)
						index = 150;
					if(mBtn.getSelection())
					{
						scheduleCells[index+1].setText(updateItem);
						scheduleCells[index+1].setBackground(instructorTable.getItem(instructorTable.getSelectionIndex()).getBackground());
					}
					if(tBtn.getSelection())
					{
						scheduleCells[index+2].setText(updateItem);
						scheduleCells[index+2].setBackground(instructorTable.getItem(instructorTable.getSelectionIndex()).getBackground());
					}
					if(wBtn.getSelection())
					{
						scheduleCells[index+3].setText(updateItem);
						scheduleCells[index+3].setBackground(instructorTable.getItem(instructorTable.getSelectionIndex()).getBackground());
					}
					if(thBtn.getSelection())
					{
						scheduleCells[index+4].setText(updateItem);
						scheduleCells[index+4].setBackground(instructorTable.getItem(instructorTable.getSelectionIndex()).getBackground());
					}
					if(fBtn.getSelection())
					{
						scheduleCells[index+5].setText(updateItem);
						scheduleCells[index+5].setBackground(instructorTable.getItem(instructorTable.getSelectionIndex()).getBackground());
					}
					
					comp.layout();
					
					
					
				}
				else if (event.widget == classList)
				{
					Course course = courses.get(classList.getSelectionIndex());
					int time = 0;
					if (course.getTimeOffered().values() != null)
						time = (Integer)course.getTimeOffered().values().toArray()[0];
					timeCombo.select(time - 8);
					roomCombo.select(roomCombo.indexOf(String.valueOf(course.getRoomNumber())));
					for (TableItem s : instructorTable.getItems())
					{

						if (s.getText().equals(course.getInstructor().toString()))
						{
							instructorTable.select(instructorTable.indexOf(s));
							break;
						}
					}
					
					if (course.getTimeOffered().containsKey(DayOfWeek.MONDAY))
						mBtn.setSelection(true);
					else
						mBtn.setSelection(false);
					
					if (course.getTimeOffered().containsKey(DayOfWeek.TUESDAY))
						tBtn.setSelection(true);
					else
						tBtn.setSelection(false);
					
					if (course.getTimeOffered().containsKey(DayOfWeek.WEDNESDAY))
						wBtn.setSelection(true);
					else
						wBtn.setSelection(false);
					
					if (course.getTimeOffered().containsKey(DayOfWeek.THURSDAY))
						thBtn.setSelection(true);
					else
						thBtn.setSelection(false);
					
					if (course.getTimeOffered().containsKey(DayOfWeek.FRIDAY))
						fBtn.setSelection(true);
					else
						fBtn.setSelection(false);
				}
			}
		};
		addClassBtn.addListener(SWT.Selection, listener);
		addInstructorBtn.addListener(SWT.Selection, listener);
		removeClassBtn.addListener(SWT.Selection, listener);
		removeInstructorBtn.addListener(SWT.Selection, listener);
		setColorBtn.addListener(SWT.Selection, listener);
		updateBtn.addListener(SWT.Selection,listener);
		importBtn.addListener(SWT.Selection, listener);
		classList.addListener(SWT.Selection, listener);
	}
	
	/**
	 * Adds a student name to the schedule in a new line with the given day, the time and the
	 * room number the student will TA for. 
	 * 
	 * @param studentName - String object that contains the name of the student to place in the schedule
	 * @param day - String object that contains the week day in which the student will TA for.
	 * @param time - Integer in which specifies the day of the time, times include 8, 9, 10, 11, 12, 1, 2 and 3.
	 * @param room -Integer which specifies the room number. These include 203, 204, and 207. 
	 */
	public void addStudentToSchedule(String studentName, String day, int time, int room)
	{
		int index = 0;
		String dayLwr = day.toLowerCase();
		
		
		//The time determines the current row and its index
		switch(time){
			case 8:
				index = 6;
				break;
			case 9:
				index = 24;
				break;
			case 10:
				index = 42;
				break;
			case 11:
				index = 60;
				break;
			case 12:
				index = 78;
				break;
			case 1:
				index = 96;
				break;
			case 2:
				index = 114;
				break;
			case 3:
				index = 132;
				break;	
		}
		
		//The day determines the column 
		if(dayLwr.equalsIgnoreCase("Monday"))
			index += 1;
		else if(dayLwr.equalsIgnoreCase("Tuesday"))
			index += 2;
		else if(dayLwr.equalsIgnoreCase("Wednesday"))
			index += 3;
		else if(dayLwr.equalsIgnoreCase("Thursday"))
			index += 4;
		else if(dayLwr.equalsIgnoreCase("Friday"))
			index += 5;
		
		//The room determines the specific cell
		switch(room){
			case 203:
				index += 0;
				break;
			case 204:
				index += 6;
				break;
			case 207:
				index += 12;
				break;
		}
		
		//Get cells current text
		String cellCurrentText = scheduleCells[index].getText();
		System.out.println("Before" + cellCurrentText);
		
		//Append to it the student name in a new line
		cellCurrentText = cellCurrentText + studentName + "\n";
		
		System.out.println("After: " + cellCurrentText);
		
		//set the text to the cell
		scheduleCells[index].setText(cellCurrentText);
	
	}
	
	/**
	 * Places all the Text objects in the composite
	 * 
	 * @param comp -  the composite object onto which to place all the text objects onto.
	 */
	private void setupSchedule(final Composite comp)
	{
		//Initialize all cells of Text widgets
		scheduleCells = new Text[SCHEDULE_COLUMNS * SCHEDULE_ROWS];
		
		for(int i = 0; i < SCHEDULE_ROWS; i++)
		{
			
			for(int j = 0; j < SCHEDULE_COLUMNS; j++)
			{
				final Text newTextField = new Text(comp, SWT.BORDER | SWT.MULTI);
				final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
				
				newTextField.setLayoutData(gridData);
				newTextField.setText(contentsList.get(i * SCHEDULE_COLUMNS + j));

				scheduleCells[i * SCHEDULE_COLUMNS + j] = newTextField;
				
				
				newTextField.addModifyListener(new ModifyListener()
				{
					@Override
					public void modifyText(ModifyEvent arg0) {
						
						
						GC gc = new GC(newTextField);
						FontMetrics fm = gc.getFontMetrics();
						
						int height = fm.getHeight();
						
						gc.dispose();

						gridData.heightHint = newTextField.getLineCount() * height;
						newTextField.setLayoutData(gridData);
						
						//Refresh layout since text boxes got larger or smaller
						comp.layout();
						
						//compute the size of the composite and set scroll bar
						comps.setMinSize(comp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
					}
				});
				
			}
		}
	}
	
	/**
	 * Initializes all the string objects that will go into each cell of the schedule
	 */
	public void setupContentsV2()
	{
		contentsList = new ArrayList<String>();
		String times[] = {"8:00", "9:00", "10:00", "11:00", "NOON", "1:00", "2:00", "3:00"};
		int timesIndex = 0;
		int timesCounter = 2;
		
		String rooms[] = {"203", "204", "207"};
		
		for(int i = 0; i < SCHEDULE_ROWS *  SCHEDULE_COLUMNS; i++)
		{
			if(i < 6)
			{
				contentsList.add(header[i]);
			}
			else if(i % 6 == 0 && timesCounter == 2)
			{
				contentsList.add(times[timesIndex]);
				timesIndex++;
				timesCounter = 0;
			}
			else if(i % 6 == 0)
			{
				contentsList.add(new String(" "));
				timesCounter++;
			}
			else
			{
				if(timesCounter == 0)
				{
					contentsList.add(rooms[0]);
				}
				else if(timesCounter == 1)
				{
					contentsList.add(rooms[1]);

				}
				else
				{
					contentsList.add(rooms[2]);
				}
			}
			
		}
		
		/*
		for(String str: contentsList)
		{
			System.out.println(str);
		}
		*/
	}

}
