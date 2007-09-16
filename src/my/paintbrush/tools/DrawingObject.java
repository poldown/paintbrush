package my.paintbrush.tools;

import my.paintbrush.controls.PbMouseListener;
import my.paintbrush.properties.Properties;

import org.eclipse.swt.widgets.Canvas;

public abstract class DrawingObject {
	
	int x0, y0;
	int x1, y1;
	
	public abstract void draw(Canvas canvas, int x1, int y1);
	
	public PbMouseListener getPbMouseListener() {
		return null;
	}
	
	// TODO: Add handling the getInstructions method of each DrawingObject
	public abstract String getInstructions();
	
	public DrawingObject(int x0, int y0, Properties prop) {
		this.x0 = this.x1 = x0;
		this.y0 = this.y1 = y0;
		/*throw new PbException("A constructor for the tool with the " +
				"following arguments: int x0, int y0, Properties prop " +
				"must be provided.");*/
	}
}
