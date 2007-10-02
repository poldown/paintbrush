package my.paintbrush.PbControls;

import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.GCData;

public class PbDrawable implements Drawable {

	public Drawable drawable;
	public int width, height;
	
	public PbDrawable(Drawable drawable, int width, int height) {
		this.drawable = drawable;
		System.out.println("\tNew PbDrawable with dimentions: " + width + "x" + height);
		this.width = width;
		this.height = height;
	}

	public void internal_dispose_GC(int handle, GCData data) {
		drawable.internal_dispose_GC(handle, data);
	}
	
	public int internal_new_GC(GCData data) {
		return drawable.internal_new_GC(data);
	}
}
