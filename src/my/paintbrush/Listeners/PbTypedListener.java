package my.paintbrush.Listeners;

import my.paintbrush.PbSWT;
import my.paintbrush.Events.PbControlEvent;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.internal.SWTEventListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class PbTypedListener implements Listener {
	
	/**
	 * The receiver's event listener
	 */
	protected SWTEventListener eventListener;

	/**
	 * Constructs a new instance of this class for the given event listener.
	 *
	 * @param listener the event listener to store in the receiver
	 */
	public PbTypedListener (SWTEventListener listener) {
		eventListener = listener;
	}
	
	/**
	 * Returns the receiver's event listener.
	 *
	 * @return the receiver's event listener
	 */
	public SWTEventListener getEventListener () {
		return eventListener;
	}
	
	/**
	 * Handles the given event.
	 * 
	 * @param e the event to handle
	 */
	public void handleEvent (Event e) {
		switch (e.type) {
			case PbSWT.MouseDoubleClick: {
				((DrawListener) eventListener).mouseDoubleClick(new MouseEvent(e));
				break;
			}
			case PbSWT.MouseDown: {
				((DrawListener) eventListener).mouseDown(new MouseEvent(e));
			}
			case PbSWT.MouseMove: {
				((DrawListener) eventListener).mouseMove(new MouseEvent(e));
				return;
			}
			case PbSWT.MouseUp: {
				((DrawListener) eventListener).mouseUp(new MouseEvent(e));
				break;
			}
			case PbSWT.Paint: {
				/* Fields set by Control */
				PaintEvent event = new PaintEvent (e);
				((DrawListener) eventListener).paintControl (event);
				e.gc = event.gc;
				break;
			}
			case PbSWT.PropertiesChanged: {
				PbControlEvent event = (PbControlEvent) e;
				event.item = e.widget;
				((PbControlsListener) eventListener).propertiesChanged (event);
				break;
			}
			case PbSWT.ToolSelected: {
				((PbControlsListener) eventListener).toolSelected((PbControlEvent) e);
				break;
			}
		}
	}
}
