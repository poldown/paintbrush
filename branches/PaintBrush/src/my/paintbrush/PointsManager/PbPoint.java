package my.paintbrush.PointsManager;

import my.paintbrush.PointsManager.IDGenerator.ID;

import org.eclipse.swt.graphics.Point;

public final class PbPoint {
	/**
	 * the x coordinate of the point
	 */
	public int x;
	
	/**
	 * the y coordinate of the point
	 */
	public int y;
	
	public ID id;
	
	/**
	 * Constructs a new point with the given x and y coordinates.
	 *
	 * @param x the x coordinate of the new point
	 * @param y the y coordinate of the new point
	 */
	public PbPoint (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void update (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Compares the argument to the receiver, and returns true
	 * if they represent the <em>same</em> object using a class
	 * specific comparison.
	 *
	 * @param object the object to compare with this object
	 * @return <code>true</code> if the object is the same as this object and <code>false</code> otherwise
	 *
	 * @see #hashCode()
	 */
	public boolean equals (Object object) {
		if (object == this) return true;
		if (!(object instanceof Point)) return false;
		Point p = (Point)object;
		return (p.x == this.x) && (p.y == this.y);
	}
	
	/**
	 * Returns an integer hash code for the receiver. Any two 
	 * objects that return <code>true</code> when passed to 
	 * <code>equals</code> must return the same value for this
	 * method.
	 *
	 * @return the receiver's hash
	 *
	 * @see #equals(Object)
	 */
	public int hashCode () {
		return x ^ y;
	}
	
	/**
	 * Returns a string containing a concise, human-readable
	 * description of the receiver.
	 *
	 * @return a string representation of the point
	 */
	public String toString () {
		return "Point {" + x + ", " + y + "}";
	}
	
	public void assignID(ID id) {
		this.id = id;
	}
}
