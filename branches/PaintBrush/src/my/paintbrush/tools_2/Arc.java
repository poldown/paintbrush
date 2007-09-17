package my.paintbrush.tools;

import my.paintbrush.properties.BasicProperties;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;

public class Arc extends DrawingObject {

	int width;
	int[] lineDash;
	Color fColor, bColor;
	
	public Arc(int x0, int y0, BasicProperties prop) {
		super(x0, y0, prop);
		this.width = (Integer)prop.getProperty(BasicProperties.WIDTH);
		this.lineDash = (int[])prop.getProperty(BasicProperties.LINEDASH);
		this.fColor = (Color)prop.getProperty(BasicProperties.FCOLOR);
	}
	
	public void draw(Canvas canvas, int x1, int y1) {
		if (this.fColor != null) {
			GC gc = new GC(canvas);
			gc.setLineWidth(this.width);
			gc.setLineDash(this.lineDash);
			gc.setForeground(canvas.getBackground());
			gc.drawArc(this.x0, this.y0, this.x1, this.y1, 30, 30);
			if (x1 != -1 && y1 != -1) {
				this.x1 = x1;
				this.y1 = y1;
			}
			gc.setForeground(this.fColor);
			gc.drawArc(this.x0, this.y0, this.x1, this.y1, 30, 30);
			gc.dispose();
		}
	}
	
	public String getInstructions() {
		return "Draws arcs";
	}
}
