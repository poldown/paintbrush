package my.paintbrush.tools;

import java.util.ArrayList;
import java.util.List;

import my.paintbrush.properties.Properties;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;

public class FreeDraw implements DrawingObject {

	List<Integer> points;
	int width;
	Color fColor, bColor;
	
	public FreeDraw(int x0, int y0, Properties prop) {
		points = new ArrayList<Integer>();
		points.add(x0);
		points.add(y0);
		this.width = prop.width;
		this.fColor = prop.fColor;
		this.bColor = prop.bColor;
	}
	
	public void draw(Canvas canvas, int x1, int y1) {
		GC gc = new GC(canvas);
		gc.setLineWidth(this.width);
		gc.setForeground(this.fColor);
		if (x1 != -1 && y1 != -1) {
			points.add(x1);
			points.add(y1);
			int size = (points.size() >= 6?6:points.size());
			gc.drawPolyline(getIntArray(points, size));
		} else
			gc.drawPolyline(getIntArray(points, points.size()));
		gc.dispose();
	}
	
	private int[] getIntArray(List<Integer> list, int howMany) {
		int[] intArr = new int[howMany];
		for (int i = 0; i < howMany; i++)
			intArr[i] = list.get(list.size() - howMany + i);
		return intArr;
	}

}
