package my.paintbrush.Tools;

import my.paintbrush.DrawingObject.DrawingObject;

public class MaskedDrawingObject {

	public DrawingObject base;
	public DrawingObject mask1;
	public DrawingObject mask2;
	
	public MaskedDrawingObject(DrawingObject base, DrawingObject mask1, DrawingObject mask2) {
		this.base = base;
		this.mask1 = mask1;
		this.mask2 = mask2;
	}
	
}
