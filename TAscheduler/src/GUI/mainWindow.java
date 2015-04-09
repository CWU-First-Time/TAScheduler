package GUI;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.ExpandableComposite;


public class mainWindow {

	protected Shell shell;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			mainWindow window = new mainWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(509, 385);
		shell.setText("SWT Application");
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");
		
		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);
		
		MenuItem mntmOpen = new MenuItem(menu_1, SWT.NONE);
		mntmOpen.setText("Open");
		
		MenuItem mntmSave = new MenuItem(menu_1, SWT.NONE);
		mntmSave.setText("Save");
		
		MenuItem mntmExit = new MenuItem(menu_1, SWT.NONE);
		mntmExit.setText("Exit");
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(0, 0, 493, 327);
		
		TabItem tbtmSchedule_1 = new TabItem(tabFolder, SWT.NONE);
		tbtmSchedule_1.setText("Schedule");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmSchedule_1.setControl(composite_1);
		
		Label lblNewLabel = new Label(composite_1, SWT.NONE);
		lblNewLabel.setBounds(71, 82, 55, 15);
		lblNewLabel.setText("New Label");
		
		TabItem tbtmStudent = new TabItem(tabFolder, SWT.NONE);
		tbtmStudent.setText("Student");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmStudent.setControl(composite);
		
		Label lblName = new Label(composite, SWT.NONE);
		lblName.setAlignment(SWT.RIGHT);
		lblName.setBounds(20, 13, 55, 15);
		lblName.setText("Name:");
		
		text = new Text(composite, SWT.BORDER);
		text.setBounds(81, 10, 150, 21);
		
		text_1 = new Text(composite, SWT.BORDER);
		text_1.setBounds(81, 37, 150, 21);
		
		Label lblLastName = new Label(composite, SWT.NONE);
		lblLastName.setAlignment(SWT.RIGHT);
		lblLastName.setBounds(10, 43, 65, 15);
		lblLastName.setText("Last Name:");
		
		text_2 = new Text(composite, SWT.BORDER);
		text_2.setBounds(81, 64, 150, 21);
		
		Label lblNewLabel_2 = new Label(composite, SWT.NONE);
		lblNewLabel_2.setAlignment(SWT.RIGHT);
		lblNewLabel_2.setBounds(10, 67, 63, 15);
		lblNewLabel_2.setText("Id:");
		
		text_3 = new Text(composite, SWT.BORDER);
		text_3.setBounds(81, 91, 150, 21);
		
		Label lblEmail = new Label(composite, SWT.NONE);
		lblEmail.setAlignment(SWT.RIGHT);
		lblEmail.setBounds(10, 97, 65, 15);
		lblEmail.setText("E-mail:");
		
		TabItem tbtmInstructor = new TabItem(tabFolder, SWT.NONE);
		tbtmInstructor.setText("Instructor");
		
		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tbtmInstructor.setControl(composite_2);
		
		Button btnNewButton_1 = new Button(composite_2, SWT.NONE);
		btnNewButton_1.setBounds(38, 58, 75, 25);
		btnNewButton_1.setText("New Button");
		
		TabItem tbtmConfiguration = new TabItem(tabFolder, SWT.NONE);
		tbtmConfiguration.setText("Configuration");
		
		Composite composite_3 = new Composite(tabFolder, SWT.NONE);
		tbtmConfiguration.setControl(composite_3);
		
		Label lblNewLabel_1 = new Label(composite_3, SWT.NONE);
		lblNewLabel_1.setBounds(44, 94, 55, 15);
		lblNewLabel_1.setText("New Label");
		
		TabItem tbtmStudentList = new TabItem(tabFolder, SWT.NONE);
		tbtmStudentList.setText("Student List");
		
		Composite composite_4 = new Composite(tabFolder, SWT.NONE);
		tbtmStudentList.setControl(composite_4);

	}
}
