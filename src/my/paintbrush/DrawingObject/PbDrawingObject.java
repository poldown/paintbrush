package my.paintbrush.DrawingObject;

import my.paintbrush.DrawingObject.DrawingObject;

public class PbDrawingObject {

	public DrawingObject base;
	public DrawingObject mask1;
	public DrawingObject mask2;
	
	public String name;
	
	public float rotationAngle = 0;
	
	public PbDrawingObject(String name, DrawingObject base, DrawingObject mask1, DrawingObject mask2) {
		this.name = name;
		this.base = base;
		this.mask1 = mask1;
		this.mask2 = mask2;
	}
	
}
