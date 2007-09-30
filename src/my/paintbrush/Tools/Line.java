package my.paintbrush.Tools;

import my.paintbrush.PointsManager.PointsManager;
import my.paintbrush.Properties.BasicProperties;

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
<<<<<<< .mine
			//gc.setAlpha(this.fColor_Trans);
=======
			//gc.setAlpha(128);
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
			/*gc.setForeground(canvas.getBackground());
			gc.drawLine(this.x0, this.y0, this.x1, this.y1);*/
>>>>>>> .r27
			if (x1 != -1 && y1 != -1) {
				this.x1 = x1;
				this.y1 = y1;
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
