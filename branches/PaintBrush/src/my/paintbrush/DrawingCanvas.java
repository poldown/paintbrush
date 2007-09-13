package my.paintbrush;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import my.paintbrush.tools.DrawingObject;
import my.paintbrush.tools.DrawingTool;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class DrawingCanvas extends Canvas {

	public DrawingTool drawingTool = DrawingTool.NONE;
	
	public java.util.List<DrawingObject> drawingObjects = new ArrayList<DrawingObject>();
	
	private SWTContent swt;
	
	public DrawingCanvas (Composite parent, int style, SWTContent swt) {
		super (parent, style);
		this.swt = swt;
		this.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		addDrawListener(this);
	}
	
	public void addDrawListener(final Canvas canvas) {
		canvas.addMouseListener(new MouseListener() {
			public void mouseUp(MouseEvent e) {
				paintAll();
				drawingTool = DrawingTool.NONE;
			}
			public void mouseDown(MouseEvent e) {
				drawingTool = swt.toolSel.getSelectedTool();
				if (drawingTool != DrawingTool.NONE) {
					try {
						Class<? extends DrawingObject> tool = Class.forName(drawingTool.className).asSubclass(DrawingObject.class);
						@SuppressWarnings("unchecked")
						Constructor<? extends DrawingObject> cons = tool.getConstructor(
								Integer.TYPE,
								Integer.TYPE,
								Class.forName(drawingTool.propertiesClassName));
						drawingObjects.add(cons.newInstance(e.x, e.y, 
								swt.toolSel.propComp.getCurProps()));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
			public void mouseDoubleClick(MouseEvent e) {}
		});
		
		canvas.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent e) {
				if (drawingTool != DrawingTool.NONE && 
						drawingObjects.size() > 0) {
					DrawingObject obj = drawingObjects.get(drawingObjects.size() - 1);
					obj.draw(canvas, e.x, e.y);
				}
			}
		});
		
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				paintAll();
			}
		});
	}
	
	public void paintAll() {
		for (DrawingObject obj : drawingObjects)
			obj.draw(this, -1, -1);
	}
}
