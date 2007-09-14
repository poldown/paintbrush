package my.paintbrush.tools;

import my.paintbrush.properties.RoundRectangleProperties;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;

public class RoundRectangle extends DrawingObject {

	int width;
	Color fColor, bColor;
	int arcW, arcH;
	
	public RoundRectangle(int x0, int y0, RoundRectangleProperties prop) {
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x0;
		this.y1 = y0;
		this.width = (Integer)prop.getProperty(RoundRectangleProperties.WIDTH);
		this.fColor = (Color)prop.getProperty(RoundRectangleProperties.FCOLOR);
		this.bColor = (Color)prop.getProperty(RoundRectangleProperties.BCOLOR);
		this.arcW = (Integer)prop.getProperty(RoundRectangleProperties.ARCW);
		this.arcH = (Integer)prop.getProperty(RoundRectangleProperties.ARCH);
	}
	
	public void draw(Canvas canvas, int x1, int y1) {
		GC gc = new GC(canvas);
		gc.setLineWidth(this.width);
		drawRoundRectangle(gc, canvas.getBackground(), canvas.getBackground());
		if (x1 != -1 && y1 != -1) {
			this.x1 = x1;
			this.y1 = y1;
		}
		drawRoundRectangle(gc, this.bColor, this.fColor);
		gc.dispose();
	}

	private void drawRoundRectangle(GC gc, Color bColor, Color fColor) {
		if (this.bColor != null) {
			gc.setBackground(bColor);
			gc.fillRoundRectangle(this.x0, this.y0, this.x1 - this.x0, this.y1 - this.y0, this.arcW, this.arcH);
		}
		if (this.fColor != null) {
			gc.setForeground(fColor);
			gc.drawRoundRectangle(this.x0, this.y0, this.x1 - this.x0, this.y1 - this.y0, this.arcW, this.arcH);
		}
	}
	
	public String getInstructions() {
		return "Draws round rectangles";
	}
}
