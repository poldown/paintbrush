package my.paintbrush.DrawingObject;

import my.paintbrush.Properties.BasicProperties;

import org.eclipse.swt.graphics.Color;

public abstract class BasicDrawingObject extends EmptyDrawingObject {
	
	protected int width;
	protected int[] lineDash;
	protected Color fColor;
	protected int fColor_Trans;
	
	public BasicDrawingObject(int x0, int y0, BasicProperties prop) {
		super(x0, y0, prop);
		this.width = (Integer)prop.getProperty(BasicProperties.WIDTH);
		this.lineDash = (int[])prop.getProperty(BasicProperties.LINEDASH);
		this.fColor = (Color)prop.getProperty(BasicProperties.FCOLOR);
		this.fColor_Trans = (Integer)prop.getProperty(BasicProperties.FCOLOR_TRANS);
	}
}
