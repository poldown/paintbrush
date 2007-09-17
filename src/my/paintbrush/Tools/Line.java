package my.paintbrush.Tools;

import my.paintbrush.PointsManager.PointsManager;
import my.paintbrush.Properties.BasicProperties;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;

public class Line extends DrawingObject {

	int width;
	int[] lineDash;
	Color fColor, bColor;
	/*Image image;
	Path path;*/
	
	public Line(int x0, int y0, BasicProperties prop) {
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
			/*if (image != null && path != null) {
				//Path path = new Path(Display.getCurrent());
				//path.moveTo(x0, y0);
				//path.lineTo(this.x1, this.y1);
				//gc.setClipping(path);
				//gc.drawImage(image, Math.min(x0, this.x1) - this.width, Math.min(y0, this.y1) - this.width);
				gc.setClipping(path);
				gc.drawImage(image, 0, 0);
				//gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
				//gc.drawLine(0, 0, 1000, 1000); 
				gc.setClipping((Path)null);
				gc.fillPath(path);
			}*/
			gc.setForeground(canvas.getBackground());
			gc.drawLine(this.x0, this.y0, this.x1, this.y1);
			if (x1 != -1 && y1 != -1) {
				this.x1 = x1;
				this.y1 = y1;
				/*//A failing try to draw the background benieth
				//the line.
				if (path != null)
					path.dispose();
				path = new Path(Display.getCurrent());
				path.addRectangle(Math.min(x0, x1) - 5, Math.min(y0, y1), Math.abs(x1 - x0) + 10, Math.abs(y1 - y0) + 10);
				//gc.setClipping(path);
				//image = new Image(Display.getCurrent(), Math.abs(x1 - x0) + 10, Math.abs(y1 - y0) + 10);
				if (image != null)
					image.dispose();
				image = new Image(Display.getCurrent(), canvas.getSize().x, canvas.getSize().y);
				//gc.copyArea(image, Math.min(x0, x1) - this.width, Math.min(y0, y1) - this.width);
				gc.copyArea(image, 0, 0);
				//gc.setClipping((Path)null);*/
			}
			gc.setForeground(this.fColor);
			gc.drawLine(this.x0, this.y0, this.x1, this.y1);
			gc.dispose();
		}
	}
	
	public PointsManager getPointsManager() {
		PointsManager pointsManager = 
			new PointsManager(PointsManager.LineMode);
		pointsManager.linkDrawingObject(this);
		return pointsManager;
	}
	
	public String getInstructions() {
		return "Draws lines";
	}
}
