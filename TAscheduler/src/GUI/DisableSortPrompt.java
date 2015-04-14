package GUI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class DisableSortPrompt extends Shell {

	private String columnName;
	boolean sort = false;
	DisableSortPrompt THIS;

	/**
	 * Create the shell.
	 * @param display
	 */
	public DisableSortPrompt(Shell parentShell, String columnName) {
		super(parentShell);
		
		this.columnName = columnName;
		THIS = this;
		
		Button disableButton = new Button(this, SWT.NONE);
		disableButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				sort = false;
				THIS.dispose();
			}
		});
		disableButton.setBounds(30, 152, 119, 68);
		disableButton.setText("Enable/Disable");
		
		Button sortButton = new Button(this, SWT.NONE);
		sortButton.setBounds(279, 152, 119, 68);
		sortButton.setText("Sort");
		sortButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				sort = true;
				THIS.dispose();
			}
		});
		
		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setBounds(30, 10, 368, 136);
		lblNewLabel.setText("Would you like to sort or enable/disable the column for CS " + columnName + "?");
		createContents();
	}
	
	public boolean getSort() {
		
		return sort;
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("SWT Application");
		setSize(450, 300);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
