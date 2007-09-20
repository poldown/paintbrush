package my.paintbrush.Tools;

import my.paintbrush.Controls.PbDo;
import my.paintbrush.Properties.BasicProperties;
import my.paintbrush.Properties.SimpleProperties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

public class Pointer extends DrawingObject {

	Color fColor;
	
	public Pointer(int x0, int y0, BasicProperties prop) {
		super(x0, y0, prop);
		this.fColor = (Color)prop.getProperty(SimpleProperties.FCOLOR);
	}
	
	@Override
	public void draw(Drawable drawable, int x1, int y1) {
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

	@Override
	public String getInstructions() {
		return "Selects Objects";
	}

}
