package my.paintbrush.Tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import my.paintbrush.Listeners.PbMouseListener;
import my.paintbrush.PbControls.PbDrawable;
import my.paintbrush.PointsManager.PbPoint;
import my.paintbrush.PointsManager.PointsManager;
import my.paintbrush.Properties.SimpleProperties;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.GC;

public class FreePolygon extends DrawingObject {

	int width;
	int[] lineDash;
	Color fColor, bColor;
	int fColor_Trans, bColor_Trans;
	List<PbPoint> points;
	
	public FreePolygon(int x0, int y0, SimpleProperties prop) {
		super(x0, y0, prop);
		this.points = new ArrayList<PbPoint>();
		this.points.add(new PbPoint(x0, y0));
		this.width = (Integer)prop.getProperty(SimpleProperties.WIDTH);
		this.lineDash = (int[])prop.getProperty(SimpleProperties.LINEDASH);
		this.fColor = (Color)prop.getProperty(SimpleProperties.FCOLOR);
		this.fColor_Trans = (Integer)prop.getProperty(SimpleProperties.FCOLOR_TRANS);
		this.bColor = (Color)prop.getProperty(SimpleProperties.BCOLOR);
		this.bColor_Trans = (Integer)prop.getProperty(SimpleProperties.BCOLOR_TRANS);
	}
	
	public void draw(Drawable drawable, int x1, int y1) {
		if (this.fColor != null) {
			GC gc = new GC(drawable);
			gc.setLineWidth(this.width);
			gc.setLineDash(this.lineDash);
			int[] intArr = getIntArray(points, points.size());
			if (x1 != -1 && y1 != -1) {
				int[] newIntArr = new int[intArr.length + 2];
				for (int i = 0; i < intArr.length; i++)
					newIntArr[i] = intArr[i];
				newIntArr[intArr.length] = this.x1;
				newIntArr[intArr.length + 1] = this.y1;
				this.x1 = x1;
				this.y1 = y1;
				newIntArr[intArr.length] = x1;
				newIntArr[intArr.length + 1] = y1;
				drawPolygon(gc, newIntArr, this.fColor, this.bColor);
			} else
				drawPolygon(gc, intArr, this.fColor, this.bColor);
			gc.dispose();
		}
	}
	
	private void drawPolygon(GC gc, int[] intArr, Color fColor, Color bColor) {
		if (this.bColor_Trans > 0) {
			gc.setBackground(bColor);
			gc.fillPolygon(intArr);
		}
		if (this.fColor_Trans > 0) {
			gc.setForeground(fColor);
			gc.drawPolygon(intArr);
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
	
	public PbMouseListener getPbMouseListener() {
		return new PbMouseListener() {
			public void mouseUp(MouseEvent e) {}
			public void mouseDown(MouseEvent e) {
				points.add(new PbPoint(e.x, e.y));
			}
			public void mouseDoubleClick(MouseEvent e) {
				points.add(new PbPoint(e.x, e.y));
				stopListening();
			}
		};
	}
	
	public void drawSample(PbDrawable drawable) {
		int maxx = drawable.width;
		int maxy = drawable.height;
		points = Arrays.asList(new PbPoint[] {
			new PbPoint(maxx / 8, maxy / 2),
			new PbPoint(maxx / 4, maxy  - maxy / 6),
			new PbPoint(maxx / 2 + maxx / 8, maxy / 8),
			new PbPoint(maxx - maxx / 8, maxy / 2)
		});
		draw(drawable, -1, -1);
	}
	
	public String getInstructions() {
		return "Draws polygons";
	}
}
