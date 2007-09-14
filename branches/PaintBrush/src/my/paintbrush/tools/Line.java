package my.paintbrush.tools;

import my.paintbrush.properties.BasicProperties;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;

public class Line extends DrawingObject {

	int width;
	Color fColor, bColor;
	
	public Line(int x0, int y0, BasicProperties prop) {
		this.x0 = x0;
		this.y0 = y0;	
		this.x1 = x0;
		this.y1 = y0;
		this.width = (Integer)prop.getProperty(BasicProperties.WIDTH);
		this.fColor = (Color)prop.getProperty(BasicProperties.FCOLOR);
	}
	
	public void draw(Canvas canvas, int x1, int y1) {
		if (this.fColor != null) {
			GC gc = new GC(canvas);
			gc.setLineWidth(this.width);
			gc.setForeground(canvas.getBackground());
			gc.drawLine(this.x0, this.y0, this.x1, this.y1);
			if (x1 != -1 && y1 != -1) {
				this.x1 = x1;
				this.y1 = y1;
			}
			gc.setForeground(this.fColor);
			gc.drawLine(this.x0, this.y0, this.x1, this.y1);
			gc.dispose();
		}
	}
	
	public String getInstructions() {
		return "Draws lines";
	}
}
