package my.paintbrush.PointsManager;

import java.util.ArrayList;
import java.util.List;

import my.paintbrush.PbException;
import my.paintbrush.DrawingObject.DrawingObject;

public class PointsManager {

	public static final int RectangleMode = 1;
	public static final int LineMode = 2;
	public static final int PointsListMode = 3;
	
	private int mode;
	private List<PbPoint> points;
	private DrawingObject drawingObject;
	
	public PointsManager(int mode) {
		if (mode != RectangleMode && 
			mode != LineMode &&
			mode != PointsListMode) {
			throw new PbException("A PointsManager was constructed " +
					"with an illegal supplied mode");
		} else {
			this.mode = mode;
		}
	}
	
	/**
	 * Note: Only for PointsListMode
	 * @param pointsList - the pointsList to link
	 */
	public void linkPointsList(List<PbPoint> pointsList) {
		if (mode == RectangleMode ||
			mode == LineMode) {
			throw new PbException("Trying to link a PointsList " +
					"while operating in Rectangle/Line Mode");
		} else {
			this.points = pointsList;
		}
	}
	
	/**
	 * Note: Only for RectangleMode
	 * @param pointsList - the pointsList to link
	 */
	public void linkDrawingObject(DrawingObject drawingObject) {
		if (mode == PointsListMode) {
			throw new PbException("Trying to link a DrawingObject " +
					"while operating in PointsList Mode");
		} else {
			this.drawingObject = drawingObject;
		}
	}
	
	private void updateRectPoints() {
		if (points == null) {
			points = new ArrayList<PbPoint>(2);
			points.add(new PbPoint(drawingObject.x0, drawingObject.y0));
			points.add(new PbPoint(drawingObject.x1, drawingObject.y0));
			points.add(new PbPoint(drawingObject.x0, drawingObject.y1));
			points.add(new PbPoint(drawingObject.x1, drawingObject.y1));
		} else {
			points.get(0).update(drawingObject.x0, drawingObject.y0);
			points.get(1).update(drawingObject.x1, drawingObject.y0);
			points.get(2).update(drawingObject.x0, drawingObject.y1);
			points.get(3).update(drawingObject.x1, drawingObject.y1);
		}
	}
	
	private void updateLinePoints() {
		if (points == null) {
			points = new ArrayList<PbPoint>(2);
			points.add(new PbPoint(drawingObject.x0, drawingObject.y0));
			points.add(new PbPoint(drawingObject.x1, drawingObject.y1));
		} else {
			points.get(0).update(drawingObject.x0, drawingObject.y0);
			points.get(1).update(drawingObject.x1, drawingObject.y1);
		}
	}
	
	public List<PbPoint> getPoints() {
		if (mode == RectangleMode)
			updateRectPoints();
		else if (mode == LineMode)
			updateLinePoints();
		return this.points;
	}
}
