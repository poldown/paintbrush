package my.paintbrush.DrawingObject;

import my.paintbrush.Listeners.PbMouseListener;
import my.paintbrush.PbControls.PbDo;
import my.paintbrush.PbControls.PbDrawable;
import my.paintbrush.PointsManager.PointsManager;
import my.paintbrush.Properties.Properties;

import org.eclipse.swt.graphics.Drawable;

public abstract class DrawingObject {
	
	public int x0, y0;
	public int x1, y1;
	
	public PointsManager pointsManager;
	
	protected boolean MASK_MODE = false;
	
	public void setMaskMode(boolean maskMode) {
		MASK_MODE = maskMode;
	}
	
	public abstract void draw(Drawable drawable, int x1, int y1);
	
	public PbMouseListener getPbMouseListener() {
		return null;
	}
	
	/**
	 * Default PointsManager
	 */
	protected PointsManager getPointsManager() {
		PointsManager pointsManager = 
			new PointsManager(PointsManager.RectangleMode);
		pointsManager.linkDrawingObject(this);
		return pointsManager;
	}
	
	/**
	 * Things to do after the creation of the object
	 */
	public PbDo doAfter() {
		return null;
	}
	
	// TODO: Add handling the getInstructions method of each DrawingObject
	public abstract String getInstructions();
	
	public DrawingObject(int x0, int y0, Properties prop) {
		this.x0 = this.x1 = x0;
		this.y0 = this.y1 = y0;
		this.pointsManager = getPointsManager();
		/*throw new PbException("A constructor for the tool with the " +
				"following arguments: int x0, int y0, Properties prop " +
				"must be provided.");*/
	}
	
	public abstract void drawSample(PbDrawable drawable);
}
