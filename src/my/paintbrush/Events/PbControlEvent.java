package my.paintbrush.Events;

import my.paintbrush.DrawingObject.DrawingTool;

import org.eclipse.swt.widgets.Event;

public class PbControlEvent extends Event {

	private static final long serialVersionUID = -8373001211668623020L;

	public PbControlEvent(Event event) {
		super();
		this.display = event.display;
		this.widget = event.widget;
		this.time = event.time;
		this.item = event.item;
		this.data = event.data;
	}
	
	public PbControlEvent() {
		super();
	}
	
	/**
	 * the item that the event occurred in (can be null)
	 */
	//public Widget item;
	
	/**
	 * a field for application use
	 */
	//public Object data;
	
	/**
	 * The Drawing tool for which the event has occured
	 */
	public DrawingTool tool;
}
