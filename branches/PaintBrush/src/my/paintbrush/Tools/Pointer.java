package my.paintbrush.Tools;

import my.paintbrush.DrawingObject.EmptyDrawingObject;
import my.paintbrush.PbControls.PbDo;
import my.paintbrush.PbControls.PbDrawable;
import my.paintbrush.Properties.PointerProperties;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class Pointer extends EmptyDrawingObject {

	/**
	 * data used to create a HAND cursor.
	 */
	static final byte[] HAND_SOURCE = {
		(byte)0xf9,(byte)0xff,(byte)0xff,(byte)0xff,
		(byte)0xf0,(byte)0xff,(byte)0xff,(byte)0xff,
		(byte)0xf0,(byte)0xff,(byte)0xff,(byte)0xff,
		(byte)0xf0,(byte)0xff,(byte)0xff,(byte)0xff,
		(byte)0xf0,(byte)0x3f,(byte)0xff,(byte)0xff,
		(byte)0xf0,(byte)0x07,(byte)0xff,(byte)0xff,
		(byte)0xf0,(byte)0x03,(byte)0xff,(byte)0xff,
		(byte)0xf0,(byte)0x00,(byte)0xff,(byte)0xff,

		(byte)0x10,(byte)0x00,(byte)0x7f,(byte)0xff,
		(byte)0x00,(byte)0x00,(byte)0x7f,(byte)0xff,
		(byte)0x80,(byte)0x00,(byte)0x7f,(byte)0xff,
		(byte)0xc0,(byte)0x00,(byte)0x7f,(byte)0xff,
		(byte)0xe0,(byte)0x00,(byte)0x7f,(byte)0xff,
		(byte)0xf0,(byte)0x00,(byte)0x7f,(byte)0xff,
		(byte)0xf8,(byte)0x00,(byte)0xff,(byte)0xff,
		(byte)0xfc,(byte)0x01,(byte)0xff,(byte)0xff,

		(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,
		(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,
		(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,
		(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,
		(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,
		(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,
		(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,
		(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,

		(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,
		(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,
		(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,
		(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,
		(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,
		(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,
		(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,
		(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff
	};
	
	Color fColor;
	
	public Pointer(int x0, int y0, PointerProperties prop) {
		super(x0, y0, prop);
		this.fColor = (Color)prop.getProperty(PointerProperties.SELECTION_FCOLOR);
	}
	
	private void chooseOperationMode() {
		System.out.println("choosing...");
	}

	@Override
	public void draw(Drawable drawable, int x1, int y1) {
		chooseOperationMode();
		GC gc = new GC(drawable);
		gc.setLineWidth(2);
		gc.setLineDash(new int[] {5, 2});
		if (x1 != -1 && y1 != -1) {
			this.x1 = x1;
			this.y1 = y1;
		}
		//gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		drawRectangle(gc, fColor);
		gc.dispose();
	}
	
	private void drawRectangle(GC gc, Color fColor) {
		gc.setForeground(fColor);
		gc.drawRectangle(this.x0, this.y0, this.x1 - this.x0, this.y1 - this.y0);
	}
	
	@Override
	public PbDo doAfter() {
		return new PbDo() {
			public void run() {
				deleteObject = true;
				generateSelection = true;
			}
		};
	}

	public void drawSample(PbDrawable drawable) {
		ImageData imageData = new ImageData(16, 16, 1, 
				new PaletteData(new RGB[] {
						new RGB(0, 0, 0),
						new RGB(255, 255, 255)
				}), -1, HAND_SOURCE);
		Image image = new Image(Display.getCurrent(), imageData);
		GC gc = new GC(drawable);
		gc.drawImage(image, (drawable.width - image.getBounds().width) / 2,
							(drawable.height - image.getBounds().height) / 2);
		gc.dispose();
	}
	
	@Override
	public String getInstructions() {
		return "Selects Objects";
	}

}
