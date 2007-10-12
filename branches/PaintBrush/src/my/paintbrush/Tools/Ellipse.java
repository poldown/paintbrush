package my.paintbrush.Tools;

import my.paintbrush.DrawingObject.SimpleDrawingObject;
import my.paintbrush.PbControls.PbDrawable;
import my.paintbrush.Properties.SimpleProperties;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.GC;

public class Ellipse extends SimpleDrawingObject {
	
	public Ellipse(int x0, int y0, SimpleProperties prop) {
		super(x0, y0, prop);
	}

	public void draw(Drawable drawable, int x1, int y1) {
		GC gc = new GC(drawable);
		gc.setLineWidth(this.width);
		gc.setLineDash(this.lineDash);
		//drawCircle(gc, canvas.getBackground(), canvas.getBackground());
		if (x1 != -1 && y1 != -1) {
			this.x1 = x1;
			this.y1 = y1;
		}
		drawCircle(gc, this.bColor, this.fColor);
		gc.dispose();
	}

	private void drawCircle(GC gc, Color bColor, Color fColor) {
		if (this.bColor_Trans > 0) {
			gc.setBackground(bColor);
			gc.fillOval(this.x0, this.y0, this.x1 - this.x0, this.y1 - this.y0);
		}
		if (this.fColor_Trans > 0) {
			gc.setForeground(fColor);
			gc.drawOval(this.x0, this.y0, this.x1 - this.x0, this.y1 - this.y0);
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
		return "Draws ellipses";
	}
}
