package my.paintbrush.tools;

import my.paintbrush.properties.SimpleProperties;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;

public class Rectangle implements DrawingObject {

	int x0, y0;
	int x1, y1;
	int width;
	Color bColor, fColor;
	
	public Rectangle(int x0, int y0, SimpleProperties prop) {
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x0;
		this.y1 = y0;
		this.width = (Integer)prop.getProperty(SimpleProperties.WIDTH);
		this.fColor = (Color)prop.getProperty(SimpleProperties.FCOLOR);
		this.bColor = (Color)prop.getProperty(SimpleProperties.BCOLOR);
	}
	
	public void draw(Canvas canvas, int x1, int y1) {
		GC gc = new GC(canvas);
		gc.setLineWidth(this.width);
		drawRectangle(gc, canvas.getBackground(), canvas.getBackground());
		if (x1 != -1 && y1 != -1) {
			this.x1 = x1;
			this.y1 = y1;
		}
		drawRectangle(gc, this.fColor, this.bColor);
		gc.dispose();
	}

	private void drawRectangle(GC gc, Color fColor, Color bColor) {
		if (this.bColor != null) {
			gc.setBackground(bColor);
			gc.fillRectangle(this.x0, this.y0, this.x1 - this.x0, this.y1 - this.y0);
		}
		if (this.fColor != null) {
			gc.setForeground(fColor);
			gc.drawRectangle(this.x0, this.y0, this.x1 - this.x0, this.y1 - this.y0);
		}
	}
}
