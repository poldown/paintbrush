package my.paintbrush.controls;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import my.paintbrush.SWTContent;
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
	private PbMouseListener pbMouseListener = null;
	
	public java.util.List<DrawingObject> drawingObjects = new ArrayList<DrawingObject>();
	
	private SWTContent swt;
	
	/*private Canvas canvas2;*/
	
	public DrawingCanvas (Composite parent, int style, SWTContent swt) {
		super (parent, style);
		this.swt = swt;
		this.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		this.setBackgroundMode(SWT.INHERIT_DEFAULT);
		addDrawListener(this);
	}
	
	public void addDrawListener(final Canvas canvas) {
		canvas.addMouseListener(new MouseListener() {
			public void mouseUp(MouseEvent e) {
				if (pbMouseListener != null && pbMouseListener.isListening())
					pbMouseListener.mouseUp(e);
				else {
					/*canvas2.dispose();*/
					paintAll();
					drawingTool = DrawingTool.NONE;
				}
			}
			public void mouseDown(MouseEvent e) {
				if (pbMouseListener != null && pbMouseListener.isListening())
					pbMouseListener.mouseDown(e);
				else {
					/*canvas2 = new Canvas(canvas, SWT.NONE);
					canvas2.setSize(canvas.getSize());
					canvas2.setLocation(0, 0);
					canvas2.setBackground(null);*/
					drawingTool = swt.toolSel.getSelectedTool();
					if (drawingTool != DrawingTool.NONE) {
						try {
							Class<? extends DrawingObject> tool = Class.forName(drawingTool.className).asSubclass(DrawingObject.class);
							@SuppressWarnings("unchecked")
							Constructor<? extends DrawingObject> cons = tool.getConstructor(
									Integer.TYPE,
									Integer.TYPE,
									Class.forName(drawingTool.propertiesClassName));
							DrawingObject instance = cons.newInstance(e.x, e.y, 
									swt.toolSel.propComp.getCurProps());
							drawingObjects.add(instance);
							pbMouseListener = instance.getPbMouseListener();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}
			public void mouseDoubleClick(MouseEvent e) {
				if (pbMouseListener != null && pbMouseListener.isListening())
					pbMouseListener.mouseDoubleClick(e);
			}
		});
		
		canvas.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent e) {
				if (drawingTool != DrawingTool.NONE && 
						drawingObjects.size() > 0) {
					DrawingObject obj = drawingObjects.get(drawingObjects.size() - 1);
					obj.draw(canvas/*2*/, e.x, e.y);
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
