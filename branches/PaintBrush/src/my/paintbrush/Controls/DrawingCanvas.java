package my.paintbrush.Controls;

import java.util.ArrayList;
import java.util.List;

import my.paintbrush.DrawingObject.DrawingObject;
import my.paintbrush.DrawingObject.PbDrawingObject;
import my.paintbrush.Listeners.DrawListener;
import my.paintbrush.Listeners.PbTypedListener;
import my.paintbrush.PbControls.PbDrawable;
import my.paintbrush.PointsManager.IDGenerator;
import my.paintbrush.PointsManager.PbPoint;
import my.paintbrush.PointsManager.PointsManager;

import org.eclipse.swt.SWT;
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
		
	private IDGenerator idGenerator = new IDGenerator();
	
	//Point Drawing Modes
	private final int PDM_Regular = 0;
	private final int PDM_Background = 1;
	private final int PDM_Moving = 2;
	private final int PDM_Hidden = 3;
	
	public java.util.List<PbDrawingObject> drawingObjects = new ArrayList<PbDrawingObject>();
	public List<PbPoint> drawingPoints = new ArrayList<PbPoint>();
	
	List<DrawingObject> selectedObjects = new ArrayList<DrawingObject>();
		
	public DrawingCanvas (Composite parent, int style) {
		super (parent, style);
		this.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		this.setBackgroundMode(SWT.INHERIT_DEFAULT);
	}

	public void addDrawListener(final DrawListener listener) {
		PbTypedListener typedListener = new PbTypedListener(listener);
		addListener (SWT.MouseDown, typedListener);
		addListener (SWT.MouseUp, typedListener);
		addListener (SWT.MouseDoubleClick, typedListener);
		addListener (SWT.MouseMove, typedListener);
		addListener (SWT.Paint, typedListener);
	}
	
	protected void generateSelection(PbDrawingObject selectObj) {
		org.eclipse.swt.graphics.Rectangle rect = new Rectangle(
				Math.min(selectObj.base.x0, selectObj.base.x1), 
				Math.min(selectObj.base.y0, selectObj.base.y1),
				Math.abs(selectObj.base.x1 - selectObj.base.x0),
				Math.abs(selectObj.base.y1 - selectObj.base.y0));
		if (rect.height == 0 && rect.width == 0) {
			PbDrawingObject objToSelect = null;
			ImageData maskImageData = new ImageData(
					this.getSize().x, this.getSize().y, 1, 
					new PaletteData(new RGB[] {
							new RGB(0, 0, 0), 
							new RGB(0xFF, 0xFF, 0xFF)
					})
			);
			for (int i = drawingObjects.size() - 1; i >= 0; i--) {
				PbDrawingObject d_obj = drawingObjects.get(i);
				Image mask = new Image(Display.getCurrent(), maskImageData);
				d_obj.mask1.draw(mask, -1, -1);
				ImageData imageData = mask.getImageData();
				mask.dispose();
				if (imageData.getPixel(rect.x, rect.y) != 0)
					if (!d_obj.equals(selectObj)) {
						objToSelect = d_obj;
						break;
					}
			}
			if (objToSelect != null) {
				moveObjectToFront(objToSelect);
				selectObject(objToSelect.base);
			}
		} else {
			for (PbDrawingObject d_obj : drawingObjects) {
				boolean select = true;
				for (PbPoint point : d_obj.base.pointsManager.getPoints())
					if (!rect.contains(point.x, point.y))
						select = false;
				if (select)
					selectObject(d_obj.base);
			}
		}
	}

	private void moveObjectToFront(PbDrawingObject dObj) {
		drawingObjects.add(dObj);
		drawingObjects.remove(dObj);
		paintAll();
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
		for (PbDrawingObject obj : drawingObjects)
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
		//System.out.println("Drawing point: " + point + " in mode: " + mode);
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
	
	public void selectAllObjects() {
		for (PbDrawingObject obj : drawingObjects)
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
		System.out.print("Is point: " + point + " hidden? ");
		for (int i = drawingObjects.size() - 1; (i >= 0) && !drawingObjects.get(i).base.equals(point.drawingObject); i--) {
			PbDrawingObject d_obj = drawingObjects.get(i);
			Image mask = new Image(Display.getCurrent(), getMaskImageData());
			d_obj.mask1.draw(mask, -1, -1);
			ImageData imageData = mask.getImageData();
			mask.dispose();
			if (imageData.getPixel(point.x, point.y) != 0) {
				hidden = true;
				break;
			}
		}
		System.out.println(hidden);
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
	
	protected void drawNotAvailable() {
		drawNotAvailable(this);
	}
	
	public static void drawNotAvailable(Canvas canvas) {
		GC gc = new GC(canvas);
		gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		gc.drawLine(0, 0, canvas.getClientArea().width, canvas.getClientArea().height);
		gc.drawLine(canvas.getClientArea().width, 0, 0, canvas.getClientArea().height);
		gc.dispose();
	}
	
	public static void drawNotAvailable(PbDrawable drawable) {
		GC gc = new GC(drawable);
		gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		gc.drawLine(0, 0, drawable.width, drawable.height);
		gc.drawLine(drawable.width, 0, 0, drawable.height);
		gc.dispose();
	}
}