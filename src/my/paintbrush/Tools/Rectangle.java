package my.paintbrush.Tools;

import my.paintbrush.DrawingObject.SimpleDrawingObject;
import my.paintbrush.PbControls.PbDrawable;
import my.paintbrush.Properties.SimpleProperties;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.GC;

public class Rectangle extends SimpleDrawingObject {

	public Rectangle(int x0, int y0, SimpleProperties prop) {
		super(x0, y0, prop);
	}
	
	public void draw(Drawable drawable, int x1, int y1) {
		GC gc = new GC(drawable);
		gc.setLineWidth(this.width);
		gc.setLineDash(this.lineDash);
		//drawRectangle(gc, canvas.getBackground(), canvas.getBackground());
		if (x1 != -1 && y1 != -1) {
			this.x1 = x1;
			this.y1 = y1;
		}
		drawRectangle(gc, this.fColor, this.bColor);
		gc.dispose();
	}

	private void drawRectangle(GC gc, Color fColor, Color bColor) {
		if (this.bColor_Trans > 0) {
			gc.setBackground(bColor);
			gc.fillRectangle(this.x0, this.y0, this.x1 - this.x0, this.y1 - this.y0);
		}
		if (this.fColor_Trans > 0) {
			gc.setForeground(fColor);
			gc.drawRectangle(this.x0, this.y0, this.x1 - this.x0, this.y1 - this.y0);
		}
	}
	
	public void drawSample(PbDrawable drawable) {
		x0 = width / 2;
		y0 = width / 2;
		x1 = drawable.width - width / 2;
		y1 = drawable.height - width / 2;
		draw(drawable, -1, -1);
	}
	
	public String getInstructions() {
		return "Draws rectangles";
	}
}
