package my.paintbrush.Tools;

import my.paintbrush.DrawingObject.SimpleDrawingObject;
import my.paintbrush.PbControls.PbDrawable;
import my.paintbrush.Properties.RoundRectangleProperties;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.GC;

public class RoundRectangle extends SimpleDrawingObject {

	int arcW, arcH;
	
	public RoundRectangle(int x0, int y0, RoundRectangleProperties prop) {
		super(x0, y0, prop);
		this.arcW = (Integer)prop.getProperty(RoundRectangleProperties.ARCW);
		this.arcH = (Integer)prop.getProperty(RoundRectangleProperties.ARCH);
	}
	
	public void draw(Drawable drawable, int x1, int y1) {
		GC gc = new GC(drawable);
		gc.setLineWidth(this.width);
		gc.setLineDash(this.lineDash);
		//drawRoundRectangle(gc, canvas.getBackground(), canvas.getBackground());
		if (x1 != -1 && y1 != -1) {
			this.x1 = x1;
			this.y1 = y1;
		}
		drawRoundRectangle(gc, this.bColor, this.fColor);
		gc.dispose();
	}

	private void drawRoundRectangle(GC gc, Color bColor, Color fColor) {
		if (this.bColor_Trans > 0) {
			gc.setBackground(bColor);
			gc.fillRoundRectangle(this.x0, this.y0, this.x1 - this.x0, this.y1 - this.y0, this.arcW, this.arcH);
		}
		if (this.fColor_Trans > 0) {
			gc.setForeground(fColor);
			gc.drawRoundRectangle(this.x0, this.y0, this.x1 - this.x0, this.y1 - this.y0, this.arcW, this.arcH);
		}
	}
	
	public void drawSample(PbDrawable drawable) {
		x0 = width / 2 + drawable.width / 10;
		y0 = width / 2 + drawable.height / 10;
		x1 = (drawable.width * 9) / 10 - width / 2;
		y1 = (drawable.height * 9) / 10 - width / 2;
		draw(drawable, -1, -1);
	}
	
	public String getInstructions() {
		return "Draws round rectangles";
	}
}
