package my.paintbrush.Tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import my.paintbrush.PbControls.PbDrawable;
import my.paintbrush.PointsManager.PbPoint;
import my.paintbrush.Properties.BasicProperties;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.GC;

public class FreeDraw extends DrawingObject {

	List<PbPoint> points;
	int width;
	int[] lineDash;
	Color fColor;
	
	public FreeDraw(int x0, int y0, BasicProperties prop) {
		super(x0, y0, prop);
		points = new ArrayList<PbPoint>();
		points.add(new PbPoint(x0, y0));
		this.width = (Integer)prop.getProperty(BasicProperties.WIDTH);
		this.lineDash = (int[])prop.getProperty(BasicProperties.LINEDASH);
		this.fColor = (Color)prop.getProperty(BasicProperties.FCOLOR);
	}
	
	public void draw(Drawable drawable, int x1, int y1) {
		if (this.fColor != null) {
			GC gc = new GC(drawable);
			gc.setLineWidth(this.width);
			gc.setForeground(this.fColor);
			if (x1 != -1 && y1 != -1) {
				points.add(new PbPoint(x1, y1));
				//int size = (points.size() >= 3?3:points.size());
				gc.drawPolyline(getIntArray(points, points.size()/*size*/));
				//Set the 4 corners of the containing rectangle
				if (x1 > this.x1) this.x1 = x1;
				if (y1 > this.y1) this.y1 = y1;
				if (x1 < this.x0) this.x0 = x1;
				if (y1 < this.y0) this.y0 = y1;
			} else {
				//gc.setForeground(canvas.getBackground());
				//gc.drawPolyline(getIntArray(points, points.size()));
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

	public void drawSample(PbDrawable drawable) {
		int maxx = drawable.width;
		int maxy = drawable.height;
		points = Arrays.asList(new PbPoint[] {
			new PbPoint(maxx / 8, maxy / 8),
			new PbPoint(maxx / 4, maxy  - maxy / 6),
			new PbPoint(maxx / 3, maxy / 4),
			new PbPoint(maxx / 2, maxy - maxy / 4),
			new PbPoint(maxx - maxx / 4, maxy / 2),
			new PbPoint(maxx - maxx / 8, maxy - maxy / 8)
		});
		draw(drawable, -1, -1);
	}
	
	public String getInstructions() {
		return "Free drawing";
	}
}
