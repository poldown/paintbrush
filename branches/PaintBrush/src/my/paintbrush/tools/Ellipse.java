package my.paintbrush.tools;

import my.paintbrush.properties.SimpleProperties;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;

public class Ellipse extends DrawingObject {

	int width;
	int[] lineDash;
	Color fColor, bColor;
	
	public Ellipse(int x0, int y0, SimpleProperties prop) {
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x0;
		this.y1 = y0;
		this.width = (Integer)prop.getProperty(SimpleProperties.WIDTH);
		this.lineDash = (int[])prop.getProperty(SimpleProperties.LINEDASH);
		this.fColor = (Color)prop.getProperty(SimpleProperties.FCOLOR);
		this.bColor = (Color)prop.getProperty(SimpleProperties.BCOLOR);
	}

	public void draw(Canvas canvas, int x1, int y1) {
		GC gc = new GC(canvas);
		gc.setLineWidth(this.width);
		gc.setLineDash(this.lineDash);
		drawCircle(gc, canvas.getBackground(), canvas.getBackground());
		if (x1 != -1 && y1 != -1) {
			this.x1 = x1;
			this.y1 = y1;
		}
		drawCircle(gc, this.bColor, this.fColor);
		gc.dispose();
	}

	private void drawCircle(GC gc, Color bColor, Color fColor) {
		if (this.bColor != null) {
			gc.setBackground(bColor);
			gc.fillOval(this.x0, this.y0, this.x1 - this.x0, this.y1 - this.y0);
		}
		if (this.fColor != null) {
			gc.setForeground(fColor);
			gc.drawOval(this.x0, this.y0, this.x1 - this.x0, this.y1 - this.y0);
		}
	}
	
	public String getInstructions() {
		return "Draws ellipses";
	}
}
