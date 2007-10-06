package my.paintbrush.DrawingObject;

import org.eclipse.swt.graphics.Color;

import my.paintbrush.Properties.SimpleProperties;

public abstract class SimpleDrawingObject extends BasicDrawingObject {

	protected Color bColor;
	protected int bColor_Trans;
	
	public SimpleDrawingObject(int x0, int y0, SimpleProperties prop) {
		super(x0, y0, prop);
		this.bColor = (Color)prop.getProperty(SimpleProperties.BCOLOR);
		this.bColor_Trans = (Integer)prop.getProperty(SimpleProperties.BCOLOR_TRANS);
	}
}
