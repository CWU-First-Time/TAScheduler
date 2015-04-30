package GUI.schedule;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class schedule {
	
	//private Table tableSetup;
	//TableEditor editor;
	private String header[] = {"QuarterYear", "Monday", "Tuesday", "Wednesday",
						"Thursday", "Friday"};
	
	private List<String> contentsList;
	private Text scheduleCells[];
	
	//Total of 144 cells
	final int SCHEDULE_COLUMNS = 6;
	final int SCHEDULE_ROWS =25;
	
	int ROW_SELECTED = 0;
	int COLUMN_SELECTED = 1;
	
	GridLayout layout;
	Composite comp;
	
	public schedule(ScrolledComposite comps)
	{
		//GridLayout
		layout = new GridLayout(SCHEDULE_COLUMNS,true);
		
		comp = new Composite(comps, SWT.NONE);
		//comp.setBounds(0, 0, 2000, 2000);
		comps.setContent(comp);
		
		layout.horizontalSpacing = 0;
		
		//Set the layout to the composite
		comp.setLayout(layout);
		
		setupContentsV2();
		setupSchedule(comp);
		
	}
	
	private void setupSchedule(final Composite comp)
	{
		//Initialize all cells of Text widgets
		scheduleCells = new Text[SCHEDULE_COLUMNS * SCHEDULE_ROWS];
		
		for(int i = 0; i < SCHEDULE_ROWS; i++)
		{
			
			for(int j = 0; j < SCHEDULE_COLUMNS; j++)
			{
				final Text newTextField = new Text(comp, SWT.BORDER_DASH | SWT.MULTI | SWT.V_SCROLL);
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
						
						comp.layout();
					}
				});
				
			}
		}
	}
	
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
	
	/*
	public void setupContents()
	{
		contentsList = new ArrayList<String[]>();
		String times[] = {"8:00", "9:00", "10:00", "11:00", "NOON", "1:00", "2:00", "3:00"};
		int timesCounter = 0;
		
		String rooms[] = {"203", "204", "207"};
		int roomsCounter = 0;
		
		int lastTimeRow = 0;
		int thisRow = 0;
		
		for(int i = 0; i < 24; i++)
		{
			String str[] = new String[6];
			thisRow = i;
			
			if(i % 3 == 0)
			{
				str[0] = times[timesCounter];
				timesCounter++;
				for(int j = 1; j < 6; j++)
				{
					str[j] = rooms[roomsCounter];
				}
				lastTimeRow = i;
				roomsCounter++;
			}
			else if(thisRow - lastTimeRow == 1)
			{
				str[0] = "";
				for(int j = 1; j < 6; j++)
				{
					str[j] = rooms[roomsCounter];
				}
				roomsCounter++;
			}
			else
			{
				str[0] = "";
				for(int j = 1; j < 6; j++)
				{
					str[j] = rooms[roomsCounter];
				
				}
				roomsCounter = 0;
			}
			contentsList.add(str);
		}
	
	}
	*/
}