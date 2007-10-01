package my.paintbrush.Tools;

import my.paintbrush.Properties.Properties;
import my.paintbrush.Properties.SimpleProperties;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.GC;

public class Rectangle extends DrawingObject {

	int width;
	int[] lineDash;
	Color bColor, fColor;
	int fColor_Trans, bColor_Trans;
	
	public Rectangle(int x0, int y0, SimpleProperties prop) {
		super(x0, y0, prop);
		this.width = (Integer)prop.getProperty(SimpleProperties.WIDTH);
		this.lineDash = (int[])prop.getProperty(SimpleProperties.LINEDASH);
		this.fColor = (Color)prop.getProperty(SimpleProperties.FCOLOR);
		this.fColor_Trans = (Integer)prop.getProperty(SimpleProperties.FCOLOR_TRANS);
		this.bColor = (Color)prop.getProperty(SimpleProperties.BCOLOR);
		this.bColor_Trans = (Integer)prop.getProperty(SimpleProperties.BCOLOR_TRANS);
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
	
	public void drawSample(Drawable drawable, Properties prop) {
		// Do nothing (TODO)
	}
	
	public String getInstructions() {
		return "Draws rectangles";
	}
}
