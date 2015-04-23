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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
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
	
	private Table tableSetup;
	TableEditor editor;
	private String header[] = {"QuarterYear", "Monday", "Tuesday", "Wednesday",
						"Thursday", "Friday"};
	
	private List<String[]> contentsList;
	int SCHEDULE_COLUMNS = 6;
	int SCHEDULE_ROWS =24; 
	int ROW_SELECTED = 0;
	int COLUMN_SELECTED = 1;
	
	public schedule(Composite comp)
	{
		
		setupContents();
		
		tableSetup = new Table(comp, SWT.BORDER | SWT.FULL_SELECTION);
		editor = new TableEditor(tableSetup);
		editor.horizontalAlignment = SWT.LEFT;
		
		setupScheduleWithNoContents(tableSetup);
		
		tableSetup.addSelectionListener(new SelectionAdapter()
		{
				public void widgetSelected(SelectionEvent e)
				{
					Control oldEditor = editor.getEditor();
					if(oldEditor != null)
						oldEditor.dispose();
					
					TableItem item = (TableItem) e.item;
					
					if(item ==  null)
						return;
					
					Text newEditor = new Text(tableSetup,SWT.NONE);
					newEditor.setText(item.getText(COLUMN_SELECTED));
					newEditor.addModifyListener(new ModifyListener(){

						@Override
						public void modifyText(ModifyEvent me) {
							// TODO Auto-generated method stub
							Text text = (Text)editor.getEditor();
							editor.getItem().setText(COLUMN_SELECTED, text.getText());
						}
					});
					newEditor.selectAll();
					newEditor.setFocus();
					editor.setEditor(newEditor, item, COLUMN_SELECTED);
				}
		}
		);
		/*
		tableSetup.addListener(SWT.MouseDown, new Listener(){
			
			@Override
			public void handleEvent(Event event)
			{
				Point pos = new Point(event.x, event.y);
				TableItem item = tableSetup.getItem(pos);
				Rectangle clientArea = tableSetup.getClientArea();
				System.out.print("Elements ");

				String temp[] = new String[6];
				for(int j = 0; j < temp.length; j++)
				{	
					temp[j] = item.getText(j);
					System.out.print(temp[j] + " ");
					
				}
				
				if(item == null)
					return;
				for(int i = 0; i < SCHEDULE_COLUMNS; i++)
				{
					Rectangle rect = item.getBounds(i);
					if(rect.contains(pos));
						int index = tableSetup.indexOf(item);
						//System.out.println("Item " + index + "-" + i);
						
						ROW_SELECTED = index;
					
				}
				
				System.out.println("\nRow: " + ROW_SELECTED + "\nColumn: " + COLUMN_SELECTED);
				
				
				int index = tableSetup.getTopIndex();
				while(index < tableSetup.getItemCount())
				{
					boolean visible = false;
					final TableItem tempItem = tableSetup.getItem(index);
					for(int k = 0; k < tableSetup.getItemCount(); k++)
					{
						Rectangle rect  = item.getBounds(k);
						if(rect.contains(pos))
						{
							final int column = k;
							final Text text  = new Text(tableSetup, SWT.NONE);
							Listener textListener = new Listener()
							{
								@Override
								public void handleEvent(final Event e)
								{
									switch(e.type)
									{
									case SWT.FocusOut:
										tempItem.setText(column, text.getText());
										text.dispose();
										break;
									case SWT.Traverse:
										switch(e.detail)
										{
										case SWT.TRAVERSE_RETURN:
											tempItem.setText(column, text.getText());
										case SWT.TRAVERSE_ESCAPE:
											text.dispose();
											e.doit = false;
										}
										break;
									
									}
								}
							};
							text.addListener(SWT.FocusOut, textListener);
							text.addListener(SWT.Traverse, textListener);
							editor.setEditor(text, tempItem, k);
							text.setText(tempItem.getText(k));
							text.selectAll();
							text.setFocus();
							return;
						}
						if(!visible && rect.intersects(clientArea))
						{
							visible = true;
						}
					}
					if(!visible) 
						return;
					index++;
				}
			}
		});
		*/
	}

	private void setupScheduleWithNoContents(Table tbl)
	{
		
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
