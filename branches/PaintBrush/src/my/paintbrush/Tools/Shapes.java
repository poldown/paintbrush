package my.paintbrush.Tools;

import java.util.ArrayList;
import java.util.List;

import my.paintbrush.DrawingObject.SimpleDrawingObject;
import my.paintbrush.PbControls.PbDrawable;
import my.paintbrush.PointsManager.PbPoint;
import my.paintbrush.PointsManager.PointsManager;
import my.paintbrush.Properties.ShapesProperties;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.GC;

public class Shapes extends SimpleDrawingObject {

	int sidesNum;
	List<PbPoint> points;
	
	public Shapes(int x0, int y0, ShapesProperties prop) {
		super(x0, y0, prop);
		this.sidesNum = (Integer)prop.getProperty(ShapesProperties.SIDESNUM);
	}
	
	public void draw(Drawable drawable, int x1, int y1) {
		GC gc = new GC(drawable);
		gc.setLineWidth(this.width);
		gc.setLineDash(this.lineDash);
		if (x1 != -1 && y1 != -1) {
			boolean pointsIsEmpty = this.points.isEmpty();
			/*if (!pointsIsEmpty)
				drawShape(gc, canvas.getBackground(), canvas.getBackground());*/
			double radius = Math.sqrt(Math.pow((x1 - this.x0), 2) + Math.pow((y1 - this.y0), 2));
			double startAng = Math.acos((x1 - x0) / radius);
			if (Math.asin((y0 - y1) / radius) > 0)
				startAng = -startAng;
			double jumps = (Math.PI * 2d) / (double)this.sidesNum;
			for (int i = 0; i < this.sidesNum; i++) {
				int x = x0 + (int)(radius * (Math.cos(startAng + jumps * i)));
				int y = y0 + (int)(radius * (Math.sin(startAng + jumps * i)));
				if (pointsIsEmpty)
					points.add(new PbPoint(x, y));
				else
					points.get(i).update(x, y);
			}
			this.x1 = x1;
			this.y1 = y1;
		}
		drawShape(gc, this.fColor, this.bColor);
		gc.dispose();
	}

	private void drawShape(GC gc, Color fColor, Color bColor) {
		if (this.bColor_Trans > 0) {
			gc.setBackground(bColor);
			gc.fillPolygon(getIntArray(points, points.size()));
		}
		if (this.fColor_Trans > 0) {
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
	
	protected PointsManager getPointsManager() {
		PointsManager pointsManager = 
			new PointsManager(PointsManager.PointsListMode);
		this.points = new ArrayList<PbPoint>();
		pointsManager.linkPointsList(this.points);
		return pointsManager;
	}
	
	public void drawSample(PbDrawable drawable) {
		x0 = drawable.width / 2;
		y0 = drawable.height / 2;
		x1 = 5 * (drawable.width / 6);
		y1 = 5 * (drawable.height / 6);
		draw(drawable, x1, y1);
	}
	
	public String getInstructions() {
		return "Draws shapes. Change the 'Number of sides' parameter " +
				"to specify how many sides should the shape have.";
	}
}
