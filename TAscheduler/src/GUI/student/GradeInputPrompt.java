package GUI.student;

import model.Grade;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class GradeInputPrompt extends Shell {

	Grade grade;
	GradeInputPrompt THIS;

	/**
	 * Create the shell.
	 * @param display
	 */
	public GradeInputPrompt(Shell parentShell, String courseName, String defaultGrade) {
		super(parentShell);

		THIS = this;

		final Combo gradeSelect = new Combo(this, SWT.NONE);
		gradeSelect.setItems(new String[]{"A", "B", "C", "D", "F", ""});

		for (int i = 0; i < Grade.values().length; i++) {
			if (grade.values()[i].toString().equals(defaultGrade)) {
				gradeSelect.select(i);
				break;
			}
		}
		
		if (gradeSelect.getSelectionIndex() < 0)
			gradeSelect.select(5);

		gradeSelect.setBounds(150, 50, 40, 40);
		
		Button okayButton = new Button(this, SWT.NONE);
		okayButton.setBounds(279, 152, 119, 68);
		okayButton.setText("Okay");
		okayButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				
				if (gradeSelect.getSelectionIndex() != 5) 
					grade = Grade.values()[gradeSelect.getSelectionIndex()];
				
				THIS.dispose();
			}
		});
		
		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setBounds(30, 10, 368, 136);
		lblNewLabel.setText("Enter Grade for CS " + courseName + "?");
		createContents();
	}
	
	public Grade getGrade() {
		
		return grade;
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
