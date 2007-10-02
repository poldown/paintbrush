package my.paintbrush.PbControls;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import my.paintbrush.SWTContent;
import my.paintbrush.Listeners.DrawListener;
import my.paintbrush.Listeners.PbMouseListener;
import my.paintbrush.PbControls.PbDo;
import my.paintbrush.PointsManager.IDGenerator;
import my.paintbrush.PointsManager.PbPoint;
import my.paintbrush.PointsManager.PointsManager;
import my.paintbrush.Properties.Properties;
import my.paintbrush.Properties.Property;
import my.paintbrush.Properties.SimpleProperties;
import my.paintbrush.Tools.DrawingObject;
import my.paintbrush.Tools.DrawingTool;
import my.paintbrush.Tools.MaskedDrawingObject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class PbDrawingCanvas extends Canvas {
	
	public DrawingTool drawingTool = DrawingTool.NONE;
	private PbMouseListener pbMouseListener = null;
	private PbMouseListener pbMouseListenerMask1 = null;
	private PbMouseListener pbMouseListenerMask2 = null;
	
	private PbDo pbDo = null;
	
	private IDGenerator idGenerator = new IDGenerator();
	
	//Point Drawing Modes
	private final int PDM_Regular = 0;
	private final int PDM_Background = 1;
	private final int PDM_Moving = 2;
	private final int PDM_Hidden = 3;
	
	public java.util.List<MaskedDrawingObject> drawingObjects = new ArrayList<MaskedDrawingObject>();
	
	public List<PbPoint> drawingPoints = new ArrayList<PbPoint>();
	
	private SWTContent swt;
	
	private Image backImage, lastPainted;
	
	public PbDrawingCanvas (Composite parent, int style, SWTContent swt) {
		super (parent, style);
		this.swt = swt;
		this.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		this.setBackgroundMode(SWT.INHERIT_DEFAULT);
		this.addDrawListener(getDrawListener());
	}
	
	private DrawListener getDrawListener() {
		return new DrawListener() {
			public void mouseUp(MouseEvent e) {
				if (pbMouseListener != null && pbMouseListener.isListening()) {
					pbMouseListener.mouseUp(e);
					pbMouseListenerMask1.mouseUp(e);
					pbMouseListenerMask2.mouseUp(e);
				} else
					if (drawingTool != DrawingTool.NONE) {
						deselectAllObjects();
						if (pbDo != null)
							handlePbDo(pbDo);
						else {
							updateBackImage();
							selectObject(drawingObjects.get(drawingObjects.size() - 1).base);
						}
						drawingTool = DrawingTool.NONE;
					}
			}
		
			public void mouseDown(MouseEvent e) {
				((PbDrawingCanvas)e.getSource()).forceFocus();
				if (pbMouseListener != null && pbMouseListener.isListening()) {
					pbMouseListener.mouseDown(e);
					pbMouseListenerMask1.mouseDown(e);
					pbMouseListenerMask2.mouseDown(e);
				} else {
					updateBackImage();
					drawingTool = swt.toolSel.getSelectedTool();
					if (drawingTool != DrawingTool.NONE) {
						try {
							Constructor<? extends DrawingObject> cons = drawingTool.getCorrespondingDOCons();
							DrawingObject instance = cons.newInstance(e.x, e.y, swt.propComp.getCurProps());
							Properties maskProp1 = changePropertiesColor(cons, new RGB(0xFF, 0xFF, 0xFF));
							DrawingObject instanceMask1 = cons.newInstance(e.x, e.y, maskProp1);
							Properties maskProp2 = changePropertiesColor(cons, new RGB(0, 0, 0));
							DrawingObject instanceMask2 = cons.newInstance(e.x, e.y, maskProp2);
							drawingObjects.add(new MaskedDrawingObject(instance, instanceMask1, instanceMask2));
							pbMouseListener = instance.getPbMouseListener();
							pbMouseListenerMask1 = instanceMask1.getPbMouseListener();
							pbMouseListenerMask2 = instanceMask2.getPbMouseListener();
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
					pbMouseListenerMask1.mouseDoubleClick(e);
					pbMouseListenerMask2.mouseDoubleClick(e);
				}
			}
			
			public void mouseMove(MouseEvent e) {
				PbDrawingCanvas canvas = (PbDrawingCanvas)e.getSource();
				if (drawingTool != DrawingTool.NONE && drawingObjects.size() > 0) {
					int index = drawingObjects.size() - 1;
					DrawingObject obj = drawingObjects.get(index).base;
					DrawingObject objMask1 = drawingObjects.get(index).mask1;
					DrawingObject objMask2 = drawingObjects.get(index).mask2;
					//drawImage(backImage, canvas);
					if (lastPainted != null && !lastPainted.isDisposed()) {
						objMask2.draw(lastPainted, e.x, e.y);
						//drawImage(lastPainted, canvas);
						Image image = new Image(Display.getCurrent(), backImage.getImageData(), lastPainted.getImageData());
						drawImage(image, canvas);
						image.dispose();
						lastPainted.dispose();
					}
					ImageData lastPaintedImageData = new ImageData(
							canvas.getSize().x, canvas.getSize().y, 1, 
							new PaletteData(new RGB[] {
									new RGB(0, 0, 0), 
									new RGB(0xFF, 0xFF, 0xFF)
							})
					);
					lastPainted = new Image(Display.getCurrent(), lastPaintedImageData);
					objMask1.draw(lastPainted, e.x, e.y);
					obj.draw(canvas, e.x, e.y);
				}
			}
		
			public void paintControl(PaintEvent e) {
				if (backImage != null && !backImage.isDisposed())
					drawImage(backImage, (PbDrawingCanvas)e.getSource());
				paintAll(new Rectangle(e.x, e.y, e.width, e.height));
			}
		};
	}

	public void addDrawListener(final DrawListener listener) {
		addListener (SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				listener.mouseDown(new MouseEvent(event));
			}});
		addListener (SWT.MouseUp, new Listener() {
			public void handleEvent(Event event) {
				listener.mouseUp(new MouseEvent(event));
			}});
		addListener (SWT.MouseDoubleClick, new Listener() {
			public void handleEvent(Event event) {
				listener.mouseDoubleClick(new MouseEvent(event));
			}});
		addListener (SWT.MouseMove, new Listener() {
			public void handleEvent(Event event) {
				listener.mouseMove(new MouseEvent(event));
			}});
		addListener (SWT.Paint, new Listener() {
			public void handleEvent(Event event) {
				listener.paintControl(new PaintEvent(event));
			}});
	}
	
	private void updateBackImage() {
		if (backImage != null)
			backImage.dispose();
		backImage = getImage(this);
	}

	private void handlePbDo(PbDo pbDo) {
		MaskedDrawingObject obj = drawingObjects.get(drawingObjects.size() - 1);
		pbDo.run();
		if (pbDo.deleteObject) {
			//drawImage(lastPainted, canvas);
			//Visible -> false
			if (lastPainted != null && !lastPainted.isDisposed()) {
				Image image = new Image(Display.getCurrent(), backImage.getImageData(), lastPainted.getImageData());
				drawImage(image, this);
				image.dispose();
				lastPainted.dispose();
			}
			//Delete actual object
			drawingObjects.remove(obj);
		}
		if (pbDo.generateSelection) {
			org.eclipse.swt.graphics.Rectangle rect = new Rectangle(
							Math.min(obj.base.x0, obj.base.x1), 
							Math.min(obj.base.y0, obj.base.y1),
							Math.abs(obj.base.x1 - obj.base.x0),
							Math.abs(obj.base.y1 - obj.base.y0));
			if (rect.height == 0 && rect.width == 0) {
				MaskedDrawingObject objToSelect = null;
				ImageData maskImageData = new ImageData(
						this.getSize().x, this.getSize().y, 1, 
						new PaletteData(new RGB[] {
								new RGB(0, 0, 0), 
								new RGB(0xFF, 0xFF, 0xFF)
						})
				);
				for (int i = drawingObjects.size() - 1; i >= 0; i--) {
					MaskedDrawingObject d_obj = drawingObjects.get(i);
					Image mask = new Image(Display.getCurrent(), maskImageData);
					d_obj.mask1.draw(mask, -1, -1);
					ImageData imageData = mask.getImageData();
					mask.dispose();
					if (imageData.getPixel(rect.x, rect.y) != 0)
						if (!d_obj.equals(obj)) {
							objToSelect = d_obj;
							break;
						}
				}
				if (objToSelect != null) {
					moveObjectToFront(objToSelect);
					selectObject(objToSelect.base);
				}
			} else {
				for (MaskedDrawingObject d_obj : drawingObjects) {
					boolean select = true;
					for (PbPoint point : d_obj.base.getPointsManager().getPoints())
						if (!rect.contains(point.x, point.y))
							select = false;
					if (select)
						selectObject(d_obj.base);
				}
			}
		}
		
	}

	private void moveObjectToFront(MaskedDrawingObject dObj) {
		drawingObjects.add(dObj);
		drawingObjects.remove(dObj);
		paintAll();
	}

	@SuppressWarnings("unchecked")
	private Properties changePropertiesColor(Constructor cons, RGB color) throws Exception {
		Class<? extends Properties> consProperties = cons.getParameterTypes()[2].asSubclass(Properties.class);
		Properties retProp = consProperties.newInstance();
		Property[] curProperties = swt.propComp.getCurProps().getProperties();
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
		paintAll(this.getClientArea());
	}
	
	public void paintAll(Rectangle rect) {
		Image workImage = new Image(Display.getCurrent(), this.getSize().x, this.getSize().y);
		for (MaskedDrawingObject obj : drawingObjects)
			obj.base.draw(workImage, -1, -1);
		GC gc = new GC(workImage);
		for (PbPoint point : drawingPoints)
			drawPoint(gc, point, (isHidden(point)?PDM_Hidden:PDM_Regular));
		gc.dispose();
		gc = new GC(this);
		gc.drawImage(workImage, rect.x, rect.y, rect.width, rect.height, 
								rect.x, rect.y, rect.width, rect.height);
		gc.dispose();
		workImage.dispose();
		System.out.println("Paint: " + rect);
	}
	
	private void drawPoint(GC gc, PbPoint point, int mode) {
		final int point_size = 3;
		if (point.id == null)
			point.assignID(idGenerator.generate());
		Display display = Display.getCurrent();
		gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		if (mode == PDM_Hidden)
			gc.setLineDash(new int[] {1});
		gc.drawOval(point.x - point_size, point.y - point_size, 
					point_size * 2, point_size * 2);
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
		if (mode != PDM_Hidden) {
		gc.fillOval(point.x - point_size + 1, point.y - point_size + 1, 
					(point_size - 1) * 2, (point_size - 1) * 2);
		gc.drawOval(point.x - point_size + 1, point.y - point_size + 1, 
				(point_size - 1) * 2, (point_size - 1) * 2);
		}
		gc.setLineDash(null);
	}
	
	List<DrawingObject> selectedObjects = new ArrayList<DrawingObject>();
	
	public void selectAllObjects() {
		for (MaskedDrawingObject obj : drawingObjects)
			selectObject(obj.base);
	}
	
	public void deselectAllObjects() {
		for (DrawingObject obj : selectedObjects)
			deselectObject(obj, false);
		selectedObjects.clear();
		paintAll();
	}
	
	public void deselectObject(DrawingObject obj, boolean remove) {
		if (selectedObjects.contains(obj)) {
			if (remove)
				selectedObjects.remove(obj);
			GC gc = new GC(this);
			PointsManager pointsManager = obj.pointsManager;
			List<PbPoint> points = pointsManager.getPoints();
			if (points != null) {
				System.out.print("DeSelect Object of type: " + obj.getClass().getSimpleName());
				for (PbPoint point : points) {
					drawingPoints.remove(point);
					drawPoint(gc, point, PDM_Background);
					System.out.print(", ID: " + point.id);
				}
				System.out.println();
			}
			gc.dispose();
			//obj.draw(this, -1, -1);
		}
	}
	
	public void selectObject(DrawingObject obj) {
		if (!selectedObjects.contains(obj)) {
			selectedObjects.add(obj);
			GC gc = new GC(this);
			PointsManager pointsManager = obj.pointsManager;
			List<PbPoint> points = pointsManager.getPoints();
			if (points != null) {
				System.out.print("Select Object of type: " + obj.getClass().getSimpleName());
				for (PbPoint point : points) {
					drawingPoints.add(point.setDO(obj));
					drawPoint(gc, point, (isHidden(point)?PDM_Hidden:PDM_Regular));
					System.out.print(", ID: " + point.id);
				}
				System.out.println();
			}
			gc.dispose();
		}
	}

	private boolean isHidden(PbPoint point) {
		boolean hidden = false;
		for (int i = drawingObjects.size() - 1; (i >= 0) && !drawingObjects.get(i).base.equals(point.drawingObject); i--) {
			MaskedDrawingObject d_obj = drawingObjects.get(i);
			Image mask = new Image(Display.getCurrent(), getMaskImageData());
			d_obj.mask1.draw(mask, -1, -1);
			ImageData imageData = mask.getImageData();
			mask.dispose();
			if (imageData.getPixel(point.x, point.y) != 0) {
				hidden = true;
				break;
			}
		}
		return (hidden);
	}

	private ImageData getMaskImageData() {
		return new ImageData(
				this.getSize().x, this.getSize().y, 1, 
				new PaletteData(new RGB[] {
						new RGB(0, 0, 0), 
						new RGB(0xFF, 0xFF, 0xFF)
				})
		);
	}
}
