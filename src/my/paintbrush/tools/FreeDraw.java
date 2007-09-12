package my.paintbrush.tools;

import java.util.ArrayList;
import java.util.List;

import my.paintbrush.properties.Properties;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;

public class FreeDraw implements DrawingObject {

	List<Point> points;
	int width;
	Color fColor, bColor;
	
	public FreeDraw(int x0, int y0, Properties prop) {
		points = new ArrayList<Point>();
		points.add(new Point(x0, y0));
		this.width = prop.width;
		this.fColor = prop.fColor;
		this.bColor = prop.bColor;
	}
	
	public void draw(Canvas canvas, int x1, int y1) {
		if (this.fColor != null) {
			GC gc = new GC(canvas);
			gc.setLineWidth(this.width);
			gc.setForeground(this.fColor);
			if (x1 != -1 && y1 != -1) {
				points.add(new Point(x1, y1));
				int size = (points.size() >= 3?3:points.size());
				gc.drawPolyline(getIntArray(points, size));
			} else
				gc.drawPolyline(getIntArray(points, points.size()));
			gc.dispose();
		}
	}
	
	private int[] getIntArray(List<Point> list, int howMany) {
		int[] intArr = new int[howMany * 2];
		System.out.println("howMany: " + howMany + " list size: " + list.size());
		for (int i = 0; i < (howMany * 2); i += 2) {
			intArr[i] = list.get(list.size() - howMany + i / 2).x;
			intArr[i + 1] = list.get(list.size() - howMany + i / 2).y;
		}
		return intArr;
	}

}
