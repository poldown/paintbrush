package my.paintbrush.Tools;

import my.paintbrush.PointsManager.PointsManager;
import my.paintbrush.Properties.BasicProperties;
import my.paintbrush.Properties.Properties;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.GC;

public class Line extends DrawingObject {

	int width;
	int[] lineDash;
	Color fColor;
	int fColor_Trans;
	
	public Line(int x0, int y0, BasicProperties prop) {
		super(x0, y0, prop);
		this.width = (Integer)prop.getProperty(BasicProperties.WIDTH);
		this.lineDash = (int[])prop.getProperty(BasicProperties.LINEDASH);
		this.fColor = (Color)prop.getProperty(BasicProperties.FCOLOR);
		this.fColor_Trans = (Integer)prop.getProperty(BasicProperties.FCOLOR_TRANS);
	}
	
	public void draw(Drawable drawable, int x1, int y1) {
		if (this.fColor != null) {
			GC gc = new GC(drawable);
			gc.setLineWidth(this.width);
			gc.setLineDash(this.lineDash);
			gc.setAlpha(this.fColor_Trans);
			if (x1 != -1 && y1 != -1) {
				this.x1 = x1;
				this.y1 = y1;
			}
			gc.setForeground(this.fColor);
			gc.drawLine(this.x0, this.y0, this.x1, this.y1);
			gc.setAlpha(-1);
			gc.dispose();
		}
	}
	
	public PointsManager getPointsManager() {
		PointsManager pointsManager = 
			new PointsManager(PointsManager.LineMode);
		pointsManager.linkDrawingObject(this);
		return pointsManager;
	}
	
	public void drawSample(Drawable drawable, Properties prop) {
		// Do nothing (TODO)
		System.out.println("Drawing Sample...");
	}
	
	public String getInstructions() {
		return "Draws lines";
	}
}
