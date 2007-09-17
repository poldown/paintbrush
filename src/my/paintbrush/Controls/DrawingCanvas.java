package my.paintbrush.Controls;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import my.paintbrush.SWTContent;
import my.paintbrush.PointsManager.IDGenerator;
import my.paintbrush.PointsManager.PbPoint;
import my.paintbrush.PointsManager.PointsManager;
import my.paintbrush.Tools.DrawingObject;
import my.paintbrush.Tools.DrawingTool;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class DrawingCanvas extends Canvas {

	public DrawingTool drawingTool = DrawingTool.NONE;
	private PbMouseListener pbMouseListener = null;
	private IDGenerator idGenerator = new IDGenerator();
	
	//Point Drawing Modes
	private final int PDM_Regular = 0;
	private final int PDM_Background = 1;
	private final int PDM_Moving = 2;
	
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
					deselectAllObjects();
					selectObject(drawingObjects.get(drawingObjects.size() - 1));
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
	
	private void drawPoint(GC gc, PbPoint point, int mode) {
		final int point_size = 2;
		
		point.assignID(idGenerator.generate());
		Display display = Display.getCurrent();
		switch (mode) {
		case PDM_Regular:
			gc.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
			gc.setBackground(display.getSystemColor(SWT.COLOR_GRAY));
			break;
		case PDM_Background:
			gc.setForeground(this.getBackground());
			gc.setBackground(this.getBackground());
			break;
		case PDM_Moving:
			gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
			gc.setBackground(display.getSystemColor(SWT.COLOR_RED));
			break;
		default:
		}
		gc.fillOval(point.x - point_size, point.y - point_size, 
					point_size * 2, point_size * 2);
		gc.drawOval(point.x - point_size, point.y - point_size, 
					point_size * 2, point_size * 2);
	}
	
	List<DrawingObject> selectedObjects = new ArrayList<DrawingObject>();
	
	public void selectAllObjects() {
		for (DrawingObject obj : drawingObjects)
			selectObject(obj);
	}
	
	public void deselectAllObjects() {
		for (DrawingObject obj : selectedObjects)
			deselectObject(obj, false);
		selectedObjects.clear();
	}
	
	public void deselectObject(DrawingObject obj, boolean remove) {
		if (selectedObjects.contains(obj)) {
			if (remove)
				selectedObjects.remove(obj);
			GC gc = new GC(this);
			PointsManager pointsManager = obj.pointsManager;
			List<PbPoint> points = pointsManager.getPoints();
			if (points != null)
				for (PbPoint point : points) {
					drawPoint(gc, point, PDM_Background);
					System.out.println("DeSelect Object - ID: " + point.id);
				}
			gc.dispose();
			obj.draw(this, -1, -1);
		}
	}
	
	public void selectObject(DrawingObject obj) {
		if (!selectedObjects.contains(obj)) {
			selectedObjects.add(obj);
			GC gc = new GC(this);
			PointsManager pointsManager = obj.pointsManager;
			List<PbPoint> points = pointsManager.getPoints();
			if (points != null)
				for (PbPoint point : points) {
					drawPoint(gc, point, PDM_Regular);
					System.out.println("Select Object - ID: " + point.id);
				}
			gc.dispose();
		}
	}
}
