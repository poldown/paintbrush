package my.paintbrush.PbControls;

import my.paintbrush.PbSWT;
import my.paintbrush.SWTContent;
import my.paintbrush.Controls.DrawingCanvas;
import my.paintbrush.DrawingObject.DrawingObject;
import my.paintbrush.DrawingObject.DrawingTool;
import my.paintbrush.Listeners.DrawListener;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class PbSampleView extends DrawingCanvas {
	
	private SWTContent swt;
	private DrawingTool drawingTool;

	public PbSampleView(Composite comp, int style, Composite parent, SWTContent swt) {
		super(comp, style);
		
		this.swt = swt;
		
		this.addDrawListener(getDrawListener());
	}

	private DrawListener getDrawListener() {
		return new DrawListener() {
			public void paintControl(PaintEvent e) {
				if (drawingTool != null)
					updateSampleView(drawingTool);
			}
			public void mouseUp(MouseEvent e) {
				System.out.println("PbSampleView: No Action (mouseUp).");
			}
			public void mouseDown(MouseEvent e) {
				System.out.println("PbSampleView: No Action (mouseDown).");
			}
			public void mouseDoubleClick(MouseEvent e) {
				System.out.println("PbSampleView: No Action (mouseDoubleClick).");
			}
			public void mouseMove(MouseEvent e) {
				System.out.println("PbSampleView: No Action (mouseMove).");
			}
		};
	}

	public void updateSampleView(DrawingTool drawingTool) {
		this.drawingTool = drawingTool;
		System.out.println("Generating Sample View using tool: " + drawingTool.disName);
		erase();
		try {
			PbDrawable drawable = new PbDrawable(this, getSize().x - 5, getSize().y - 5);
			DrawingObject instance = drawingTool.getCorrespondingDOCons().newInstance(-1, -1, swt.propComp.getCurProps());
			instance.drawSample(drawable);
		} catch (Exception e) {
			e.printStackTrace();
			drawNotAvailable();
		}
	}

	private void erase() {
		GC gc = new GC(this);
		gc.setBackground(Display.getCurrent().getSystemColor(PbSWT.COLOR_WHITE));
		gc.fillRectangle(0, 0, getSize().x, getSize().y);
		gc.dispose();
	}
}
