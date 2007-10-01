package my.paintbrush.Controls;

import java.lang.reflect.InvocationTargetException;

import my.paintbrush.Pb;
import my.paintbrush.SWTContent;
import my.paintbrush.Tools.DrawingTool;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class SampleView extends Canvas {
	
	private SWTContent swt;

	public SampleView(Composite comp, int style, Composite parent, SWTContent swt) {
		super(comp, style);
		
		this.swt = swt;
		
		Display display = Display.getCurrent();
		this.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		
		parent.addListener(Pb.PropChangeEvent, handlePropChangeEvent());
	}
	
	private Listener handlePropChangeEvent() {
		return new Listener() {
			public void handleEvent(Event event) {
				System.out.println("Properties Changed (" + event.item + ")");
				// The drawingTool which is being used must be supplied
				// as the event.data!
				updateSampleView((DrawingTool) event.data);
			}
		};
	}

	protected void updateSampleView(DrawingTool drawingTool) {
		System.out.println("Generating Sample View using tool: " + drawingTool.disName);
		// TODO: write the default constructors for each tool, so this comment
		// could be un-commented.
		/*try {
			drawingTool.getCorrespondingDODefCons().newInstance().
					drawSample(this, swt.propComp.getCurProps());
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
}
