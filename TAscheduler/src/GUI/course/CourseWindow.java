package GUI.course;

import org.eclipse.swt.widgets.Composite;

import scheduler.Scheduler;

public class CourseWindow {
	
	private Scheduler scheduler;
	
	public CourseWindow(Composite parent, Scheduler s) {
		
		if (s != null) 
			scheduler = s;
		
		else
			scheduler = new Scheduler();
		
		
	}

}
