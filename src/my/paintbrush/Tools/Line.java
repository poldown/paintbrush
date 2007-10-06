package my.paintbrush.Tools;

import my.paintbrush.DrawingObject.BasicDrawingObject;
import my.paintbrush.PbControls.PbDrawable;
import my.paintbrush.PointsManager.PointsManager;
import my.paintbrush.Properties.BasicProperties;

import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.GC;

public class Line extends BasicDrawingObject {
	
	public Line(int x0, int y0, BasicProperties prop) {
		super(x0, y0, prop);
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
	
	public void drawSample(PbDrawable drawable) {
		x0 = width * 2;
		y0 = width * 2;
		x1 = drawable.width - width * 2;
		y1 = drawable.height - width * 2;
		draw(drawable, -1, -1);
		x0 = (drawable.width / 3) * 2;
		y0 = width * 2;
		x1 = drawable.width / 3;
		y1 = drawable.height - width * 2;
		draw(drawable, -1, -1);
	}
	
	public String getInstructions() {
		return "Draws lines";
	}
}
