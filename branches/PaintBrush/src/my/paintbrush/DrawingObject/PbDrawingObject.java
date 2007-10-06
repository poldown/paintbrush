package my.paintbrush.DrawingObject;

import my.paintbrush.DrawingObject.DrawingObject;

public class PbDrawingObject {

	public DrawingObject base;
	public DrawingObject mask1;
	public DrawingObject mask2;
	
	public float rotationAngle = 0;
	
	public PbDrawingObject(DrawingObject base, DrawingObject mask1, DrawingObject mask2) {
		this.base = base;
		this.mask1 = mask1;
		this.mask2 = mask2;
	}
	
}
