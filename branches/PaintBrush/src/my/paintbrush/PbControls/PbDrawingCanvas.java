package my.paintbrush.PbControls;

import java.lang.reflect.Constructor;

import my.paintbrush.SWTContent;
import my.paintbrush.Controls.DrawingCanvas;
import my.paintbrush.DrawingObject.DrawingObject;
import my.paintbrush.DrawingObject.DrawingTool;
import my.paintbrush.Listeners.DrawListener;
import my.paintbrush.Listeners.PbMouseListener;
import my.paintbrush.Properties.Properties;
import my.paintbrush.Properties.Property;
import my.paintbrush.Tools.MaskedDrawingObject;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class PbDrawingCanvas extends DrawingCanvas {
	
	public DrawingTool drawingTool = DrawingTool.NONE;
	
	private PbMouseListener pbMouseListener = null;
	private PbMouseListener pbMouseListenerMask1 = null;
	private PbMouseListener pbMouseListenerMask2 = null;
	
	private PbDo pbDo = null;
	
	private SWTContent swt;
	
	private Image backImage, lastPainted;
	
	public PbDrawingCanvas (Composite parent, int style, SWTContent swt) {
		super (parent, style);
		this.swt = swt;
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
					deselectAllObjects();
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
					lastPainted.getImageData().maskData = null;
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
			generateSelection(obj);
		}
		
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
			if (prop.name.contains("COLOR"))
				editedVal = new Color(Display.getCurrent(), color);
			editedProps[i] = new Property(prop.name, editedVal);
		}
		retProp.properties = editedProps;
		return retProp;
	}
}
