package my.paintbrush.tools;

import java.util.ArrayList;
import java.util.List;

import my.paintbrush.PointsManager.PbPoint;
import my.paintbrush.PointsManager.PointsManager;
import my.paintbrush.properties.ShapesProperties;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;

public class Shapes extends DrawingObject {

	int sidesNum;
	List<PbPoint> points;
	int width;
	int[] lineDash;
	Color bColor, fColor;
	
	public Shapes(int x0, int y0, ShapesProperties prop) {
		super(x0, y0, prop);
		this.width = (Integer)prop.getProperty(ShapesProperties.WIDTH);
		this.lineDash = (int[])prop.getProperty(ShapesProperties.LINEDASH);
		this.fColor = (Color)prop.getProperty(ShapesProperties.FCOLOR);
		this.bColor = (Color)prop.getProperty(ShapesProperties.BCOLOR);
		this.sidesNum = (Integer)prop.getProperty(ShapesProperties.SIDESNUM);
	}
	
	public void draw(Canvas canvas, int x1, int y1) {
		GC gc = new GC(canvas);
		gc.setLineWidth(this.width);
		gc.setLineDash(this.lineDash);
		if (x1 != -1 && y1 != -1) {
			if (this.points != null)
				drawShape(gc, canvas.getBackground(), canvas.getBackground());
			this.points = new ArrayList<PbPoint>(this.sidesNum);
			double radius = Math.sqrt(Math.pow((x1 - this.x0), 2) + Math.pow((y1 - this.y0), 2));
			double startAng = Math.acos((x1 - x0) / radius);
			if (Math.asin((y0 - y1) / radius) > 0)
				startAng = -startAng;
			double jumps = (Math.PI * 2d) / (double)this.sidesNum;
			for (int i = 0; i < this.sidesNum; i++) {
				int x = x0 + (int)(radius * (Math.cos(startAng + jumps * i)));
				int y = y0 + (int)(radius * (Math.sin(startAng + jumps * i)));
				PbPoint point = new PbPoint(x, y);
				points.add(point);
			}
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
	
	private int[] getIntArray(List<PbPoint> list, int howMany) {
		int[] intArr = new int[howMany * 2];
		for (int i = 0; i < (howMany * 2); i += 2) {
			intArr[i] = list.get(list.size() - howMany + i / 2).x;
			intArr[i + 1] = list.get(list.size() - howMany + i / 2).y;
		}
		return intArr;
	}
	
	public PointsManager getPointsManager() {
		PointsManager pointsManager = 
			new PointsManager(PointsManager.PointsListMode);
		pointsManager.linkPointsList(this.points);
		return pointsManager;
	}
	
	public String getInstructions() {
		return "Draws shapes. Change the 'Number of sides' parameter " +
				"to specify how many sides should the shape have.";
	}
}
