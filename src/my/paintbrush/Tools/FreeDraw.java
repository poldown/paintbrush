package my.paintbrush.Tools;

import java.util.ArrayList;
import java.util.List;

import my.paintbrush.PointsManager.PbPoint;
import my.paintbrush.Properties.BasicProperties;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;

public class FreeDraw extends DrawingObject {

	List<PbPoint> points;
	int width;
	int[] lineDash;
	Color fColor, bColor;
	
	public FreeDraw(int x0, int y0, BasicProperties prop) {
		super(x0, y0, prop);
		points = new ArrayList<PbPoint>();
		points.add(new PbPoint(x0, y0));
		this.width = (Integer)prop.getProperty(BasicProperties.WIDTH);
		this.lineDash = (int[])prop.getProperty(BasicProperties.LINEDASH);
		this.fColor = (Color)prop.getProperty(BasicProperties.FCOLOR);
	}
	
	public void draw(Canvas canvas, int x1, int y1) {
		if (this.fColor != null) {
			GC gc = new GC(canvas);
			gc.setLineWidth(this.width);
			gc.setForeground(this.fColor);
			if (x1 != -1 && y1 != -1) {
				points.add(new PbPoint(x1, y1));
				int size = (points.size() >= 3?3:points.size());
				gc.drawPolyline(getIntArray(points, size));
			} else {
				gc.setForeground(canvas.getBackground());
				gc.drawPolyline(getIntArray(points, points.size()));
				gc.setLineDash(this.lineDash);
				gc.setForeground(this.fColor);
				gc.drawPolyline(getIntArray(points, points.size()));
			}
			gc.dispose();
		}
	}
	
	private int[] getIntArray(List<PbPoint> list, int howMany) {
		int[] intArr = new int[howMany * 2];
		for (int i = 0; i < (howMany * 2); i += 2) {
			intArr[i] = list.get(list.size() - howMany + i / 2).x;
			intArr[i + 1] = list.get(list.size() - howMany + i / 2).y;
		}
		return intArr;
	}

	public String getInstructions() {
		return "Free drawing";
	}
}