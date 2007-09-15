package my.paintbrush.tools;

import java.util.ArrayList;
import java.util.List;

import my.paintbrush.controls.PbMouseListener;
import my.paintbrush.properties.SimpleProperties;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;

public class FreePolygon extends DrawingObject {

	int width;
	int[] lineDash;
	Color fColor, bColor;
	List<Point> points;
	
	public FreePolygon(int x0, int y0, SimpleProperties prop) {
		this.x0 = x0;
		this.y0 = y0;	
		this.x1 = x0;
		this.y1 = y0;
		this.points = new ArrayList<Point>();
		this.points.add(new Point(x0, y0));
		this.width = (Integer)prop.getProperty(SimpleProperties.WIDTH);
		this.lineDash = (int[])prop.getProperty(SimpleProperties.LINEDASH);
		this.fColor = (Color)prop.getProperty(SimpleProperties.FCOLOR);
		this.bColor = (Color)prop.getProperty(SimpleProperties.BCOLOR);
	}
	
	public void draw(Canvas canvas, int x1, int y1) {
		if (this.fColor != null) {
			GC gc = new GC(canvas);
			gc.setLineWidth(this.width);
			gc.setLineDash(this.lineDash);
			int[] intArr = getIntArray(points, points.size());
			int[] newIntArr = new int[intArr.length + 2];
			for (int i = 0; i < intArr.length; i++)
				newIntArr[i] = intArr[i];
			newIntArr[intArr.length] = this.x1;
			newIntArr[intArr.length + 1] = this.y1;
			drawPolygon(gc, newIntArr, canvas.getBackground(), canvas.getBackground());
			if (x1 != -1 && y1 != -1) {
				this.x1 = x1;
				this.y1 = y1;
				newIntArr[intArr.length] = x1;
				newIntArr[intArr.length + 1] = y1;
			}
			drawPolygon(gc, newIntArr, this.fColor, this.bColor);
			gc.dispose();
		}
	}
	
	private void drawPolygon(GC gc, int[] intArr, Color fColor, Color bColor) {
		if (this.bColor != null) {
			gc.setBackground(bColor);
			gc.fillPolygon(intArr);
		}
		if (this.fColor != null) {
			gc.setForeground(fColor);
			gc.drawPolygon(intArr);
		}
	}
	
	private int[] getIntArray(List<Point> list, int howMany) {
		int[] intArr = new int[howMany * 2];
		for (int i = 0; i < (howMany * 2); i += 2) {
			intArr[i] = list.get(list.size() - howMany + i / 2).x;
			intArr[i + 1] = list.get(list.size() - howMany + i / 2).y;
		}
		return intArr;
	}
	
	public String getInstructions() {
		return "Draws polygons";
	}
	
	public PbMouseListener getPbMouseListener() {
		return new PbMouseListener() {
			public void mouseUp(MouseEvent e) {}
			public void mouseDown(MouseEvent e) {
				points.add(new Point(e.x, e.y));
			}
			public void mouseDoubleClick(MouseEvent e) {
				points.add(new Point(e.x, e.y));
				stopListening();
			}
		};
	}
}
