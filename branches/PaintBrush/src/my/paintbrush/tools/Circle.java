package my.paintbrush.tools;

import my.paintbrush.properties.Properties;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;

public class Circle implements DrawingObject {

	int x0, y0;
	int x1, y1;
	int width;
	Color fColor, bColor;
	
	public Circle(int x0, int y0, Properties prop) {
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x0;
		this.y1 = y0;
		this.width = prop.width;
		this.fColor = prop.fColor;
		this.bColor = prop.bColor;
	}
	
	public void draw(Canvas canvas, int x1, int y1) {
		GC gc = new GC(canvas);
		gc.setLineWidth(this.width);
		gc.setForeground(canvas.getBackground());
		gc.drawArc(this.x0, this.y0, this.x1 - this.x0, this.y1 - this.y0, 0, 360);
		if (x1 != -1 && y1 != -1) {
			this.x1 = x1;
			this.y1 = y1;
		}
		gc.setForeground(this.fColor);
		gc.drawArc(this.x0, this.y0, this.x1 - this.x0, this.y1 - this.y0, 0, 360);
		gc.dispose();
	}

}