package GUI.schedule;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
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
	
	private Table tableSetup;
	private String header[] = {"QuarterYear", "Monday", "Tuesday", "Wednesday",
						"Thursday", "Friday"};
	
	private List<String[]> contentsList;
	
	public schedule(Composite comp)
	{
		setupContents();
		
		tableSetup = new Table(comp, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		//TableEditor editor = new TableEditor(tableSetup);
		//editor.horizontalAlignment = SWT.LEFT;
		
		setupScheduleWithNoContents(tableSetup);
	}

	private void setupScheduleWithNoContents(Table tbl)
	{
		int SCHEDULE_COLUMNS = 6;
		int SCHEDULE_ROWS =24; 
		
		tbl.setLinesVisible(true);
		tbl.setHeaderVisible(true);
		
		//Set columns
		for(int i = 0; i< SCHEDULE_COLUMNS; i++)
		{
			TableColumn column = new TableColumn(tbl, SWT.NONE);
			
			if(i == 0)
				column.setWidth(80);
			else
				column.setWidth(200);
			column.setText(header[i]);
		}
		
		//Add contents
		for(int j = 0; j < SCHEDULE_ROWS; j++)
		{
			TableItem item = new TableItem(tbl, SWT.NONE);
			item.setText(contentsList.get(j));
		}
	}
	
	
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
}
