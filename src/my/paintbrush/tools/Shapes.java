package my.paintbrush.tools;

import java.util.List;

import my.paintbrush.properties.Properties;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;

public class Shapes implements DrawingObject {

	int x0, y0;
	int x1, y1;
	List<Point> points;
	int width;
	Color bColor, fColor;
	
	public Shapes(int x0, int y0, Properties prop) {
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x0;
		this.y1 = y0;
		//Add handling of the type of the shape (properties
		//should be updated)
		this.width = prop.width;
		this.fColor = prop.fColor;
		this.bColor = prop.bColor;
	}
	
	public void draw(Canvas canvas, int x1, int y1) {
		GC gc = new GC(canvas);
		gc.setLineWidth(this.width);
		drawShape(gc, canvas.getBackground(), canvas.getBackground());
		if (x1 != -1 && y1 != -1) {
			this.x1 = x1;
			this.y1 = y1;
		}
		drawShape(gc, this.fColor, this.bColor);
		gc.dispose();
	}

	private void drawShape(GC gc, Color fColor, Color bColor) {
		if (this.bColor != null) {
			gc.setBackground(bColor);
			gc.fillPolygon(getIntArray(points, points.size()));
		}
		if (this.fColor != null) {
			gc.setForeground(fColor);
			gc.drawPolygon(getIntArray(points, points.size()));
		}
	}
	
	private int[] getIntArray(List<Point> list, int howMany) {
		int[] intArr = new int[howMany * 2];
		for (int i = 0; i < (howMany * 2); i += 2) {
			intArr[i] = list.get(list.size() - howMany + i / 2).x;
			intArr[i + 1] = list.get(list.size() - howMany + i / 2).y;
		}
		return intArr;
	}
}
