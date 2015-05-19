package GUI.configuration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Spinner;

import GUI.Instructors.SWTResourceManager;

public class ConfigurationWindow extends Shell {
	private Table table;
	private Text text;
	private Text text_1;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			ConfigurationWindow shell = new ConfigurationWindow(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public ConfigurationWindow(Display display) {
		super(display, SWT.SHELL_TRIM);
		setSize(654, 412);
		setLayout(null);
		
		Label lblInstructors = new Label(this, SWT.NONE);
		lblInstructors.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 20, SWT.NORMAL));
		lblInstructors.setBounds(10, 43, 110, 28);
		lblInstructors.setText("Instructors");
		
		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 73, 140, 169);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		Button btnAdd = new Button(this, SWT.NONE);
		btnAdd.setBounds(10, 248, 95, 28);
		btnAdd.setText("Add");
		
		Button btnEdit = new Button(this, SWT.NONE);
		btnEdit.setBounds(10, 278, 95, 28);
		btnEdit.setText("Edit");
		
		Button btnDelete = new Button(this, SWT.NONE);
		btnDelete.setBounds(10, 305, 95, 28);
		btnDelete.setText("Delete");
		
		Label lblFirstName = new Label(this, SWT.NONE);
		lblFirstName.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 15, SWT.NORMAL));
		lblFirstName.setBounds(173, 72, 95, 28);
		lblFirstName.setText("First Name");
		
		text = new Text(this, SWT.BORDER);
		text.setBounds(289, 73, 95, 19);
		
		Label lblLastName = new Label(this, SWT.NONE);
		lblLastName.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 15, SWT.NORMAL));
		lblLastName.setBounds(173, 137, 78, 19);
		lblLastName.setText("Last Name");
		
		text_1 = new Text(this, SWT.BORDER);
		text_1.setBounds(289, 138, 95, 19);
		
		Label lblColor = new Label(this, SWT.NONE);
		lblColor.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 15, SWT.NORMAL));
		lblColor.setBounds(492, 72, 78, 19);
		lblColor.setText("Color");
		
		Label lblColorPalletGoes = new Label(this, SWT.NONE);
		lblColorPalletGoes.setBounds(472, 121, 142, 14);
		lblColorPalletGoes.setText("Color pallet goes here");
		
		Label lblClassMaximumHours = new Label(this, SWT.NONE);
		lblClassMaximumHours.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 16, SWT.NORMAL));
		lblClassMaximumHours.setBounds(330, 233, 166, 28);
		lblClassMaximumHours.setText("Class maximum hours");
		
		Label lblCs = new Label(this, SWT.NONE);
		lblCs.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 15, SWT.NORMAL));
		lblCs.setBounds(277, 267, 60, 14);
		lblCs.setText("CS 392");
		
		Label lblCs_1 = new Label(this, SWT.NONE);
		lblCs_1.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 15, SWT.NORMAL));
		lblCs_1.setBounds(277, 305, 60, 14);
		lblCs_1.setText("CS 492");
		
		Label lblPaidTa = new Label(this, SWT.NONE);
		lblPaidTa.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 15, SWT.NORMAL));
		lblPaidTa.setBounds(277, 345, 60, 14);
		lblPaidTa.setText("Paid TA");
		
		Spinner spinner = new Spinner(this, SWT.BORDER);
		spinner.setBounds(363, 267, 71, 28);
		
		Spinner spinner_1 = new Spinner(this, SWT.BORDER);
		spinner_1.setBounds(363, 304, 71, 28);
		
		Spinner spinner_2 = new Spinner(this, SWT.BORDER);
		spinner_2.setBounds(363, 337, 71, 28);
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}