package my.paintbrush.Controls;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import my.paintbrush.SWTContent;
import my.paintbrush.PointsManager.IDGenerator;
import my.paintbrush.PointsManager.PbPoint;
import my.paintbrush.PointsManager.PointsManager;
import my.paintbrush.Properties.Properties;
import my.paintbrush.Properties.Property;
import my.paintbrush.Properties.SimpleProperties;
import my.paintbrush.Tools.DrawingObject;
import my.paintbrush.Tools.DrawingTool;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class DrawingCanvas extends Canvas {
	
	public DrawingTool drawingTool = DrawingTool.NONE;
	private PbMouseListener pbMouseListener = null;
	private PbMouseListener pbMouseListenerMasks = null;
	private PbMouseListener pbMouseListenerMasks2 = null;
	
	private PbDo pbDo = null;
	
	private IDGenerator idGenerator = new IDGenerator();
	
	//Point Drawing Modes
	private final int PDM_Regular = 0;
	private final int PDM_Background = 1;
	private final int PDM_Moving = 2;
	
	public java.util.List<DrawingObject> drawingObjects = new ArrayList<DrawingObject>();
	public java.util.List<DrawingObject> drawingObjectsMasks = new ArrayList<DrawingObject>();
	public java.util.List<DrawingObject> drawingObjectsMasks2 = new ArrayList<DrawingObject>();
	
	private SWTContent swt;
	
	private Image backImage, lastPainted;
	
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
				if (pbMouseListener != null && pbMouseListener.isListening()) {
					pbMouseListener.mouseUp(e);
					pbMouseListenerMasks.mouseUp(e);
					pbMouseListenerMasks2.mouseUp(e);
				} else {
					deselectAllObjects();
					if (pbDo != null)
						handlePbDo(pbDo);
					else
						selectObject(drawingObjects.get(drawingObjects.size() - 1));
					drawingTool = DrawingTool.NONE;
				}
			}
			public void mouseDown(MouseEvent e) {
					if (pbMouseListener != null && pbMouseListener.isListening()) {
						pbMouseListener.mouseDown(e);
						pbMouseListenerMasks.mouseDown(e);
						pbMouseListenerMasks2.mouseDown(e);
					} else {
						if (backImage != null)
							backImage.dispose();
						backImage = getImage(canvas);
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
								Properties maskProp = changePropertiesColor(cons, new RGB(0xFF, 0xFF, 0xFF));
								DrawingObject instanceMask = cons.newInstance(e.x, e.y, maskProp);
								drawingObjectsMasks.add(instanceMask);
								Properties maskProp2 = changePropertiesColor(cons, new RGB(0, 0, 0));
								DrawingObject instanceMask2 = cons.newInstance(e.x, e.y, maskProp2);
								drawingObjectsMasks2.add(instanceMask2);
								pbMouseListener = instance.getPbMouseListener();
								pbMouseListenerMasks = instanceMask.getPbMouseListener();
								pbMouseListenerMasks2 = instanceMask2.getPbMouseListener();
								pbDo = instance.doAfter();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
					}
			}
			public void mouseDoubleClick(MouseEvent e) {
				if (pbMouseListener != null && pbMouseListener.isListening()) {
					pbMouseListener.mouseDoubleClick(e);
					pbMouseListenerMasks.mouseDoubleClick(e);
					pbMouseListenerMasks2.mouseDoubleClick(e);
				}
			}
		});
		
		canvas.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent e) {
				if (drawingTool != DrawingTool.NONE && drawingObjects.size() > 0) {
					int index = drawingObjects.size() - 1;
					DrawingObject obj = drawingObjects.get(index);
					DrawingObject objMask = drawingObjectsMasks.get(index);
					DrawingObject objMask2 = drawingObjectsMasks2.get(index);
					//drawImage(backImage, canvas);
					ImageData lastPaintedImageData = new ImageData(
							canvas.getSize().x, canvas.getSize().y, 1, 
							new PaletteData(new RGB[] {
									new RGB(0, 0, 0), 
									new RGB(0xFF, 0xFF, 0xFF)
							})
					);
					if (lastPainted != null) {
						objMask2.draw(lastPainted, e.x, e.y);
						//drawImage(lastPainted, canvas);
						Image image = new Image(Display.getCurrent(), backImage.getImageData(), lastPainted.getImageData());
						drawImage(image, canvas);
						image.dispose();
						lastPainted.dispose();
					}
					lastPainted = new Image(Display.getCurrent(), lastPaintedImageData);
					objMask.draw(lastPainted, e.x, e.y);
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
	
	private void handlePbDo(PbDo pbDo) {
		DrawingObject obj = drawingObjects.get(drawingObjects.size() - 1);
		pbDo.run();
		if (pbDo.generateSelection) {
			org.eclipse.swt.graphics.Rectangle rect = new Rectangle(obj.x0, obj.y0, obj.x1 - obj.x0, obj.y1 - obj.y0);
			for (DrawingObject d_obj : drawingObjects) {
				boolean select = true;
				for (PbPoint point : d_obj.getPointsManager().getPoints())
					if (!rect.contains(point.x, point.y))
						select = false;
				if (select)
					selectObject(d_obj);
			}
		}
		if (pbDo.deleteObject) {
			drawingObjectsMasks.remove(drawingObjects.indexOf(obj));
			drawingObjectsMasks2.remove(drawingObjects.indexOf(obj));
			drawingObjects.remove(obj);
		}
	}

	@SuppressWarnings("unchecked")
	private Properties changePropertiesColor(Constructor cons, RGB color) throws Exception {
		Class<? extends Properties> consProperties = cons.getParameterTypes()[2].asSubclass(Properties.class);
		Properties retProp = consProperties.newInstance();
		Property[] curProperties = swt.toolSel.propComp.getCurProps().getProperties();
		Property[] editedProps = new Property[curProperties.length];
		for (int i = 0; i < curProperties.length; i++) {
			Property prop = curProperties[i];
			Object editedVal = prop.value; 
			if (prop.name.equals(SimpleProperties.FCOLOR.name) || 
				prop.name.equals(SimpleProperties.BCOLOR.name))
				editedVal = new Color(Display.getCurrent(), color);
			editedProps[i] = new Property(prop.name, editedVal);
		}
		retProp.properties = editedProps;
		return retProp;
	}
	
	public void drawImage(Image image, Canvas canvas) {
		GC gc = new GC(canvas);
		gc.drawImage(image, 0, 0);
		gc.dispose();
	}
	
	public Image getImage(Canvas canvas) {
		GC gc = new GC(canvas);
		Image image = new Image(Display.getCurrent(), canvas.getSize().x, canvas.getSize().y);
		gc.copyArea(image, 0, 0);
		gc.dispose();
		return image;
	}
	
	public void paintAll() {
		for (DrawingObject obj : drawingObjects)
			obj.draw(this, -1, -1);
	}
	
	private void drawPoint(GC gc, PbPoint point, int mode) {
		final int point_size = 2;
		if (point.id == null)
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
					System.out.println("DeSelect Object of type: " + obj.getClass().getSimpleName() + ", ID: " + point.id);
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
					System.out.println("Select Object of type: " + obj.getClass().getSimpleName() + ", ID: " + point.id);
				}
			gc.dispose();
		}
	}
}
