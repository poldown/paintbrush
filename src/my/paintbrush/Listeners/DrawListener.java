package my.paintbrush.Listeners;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.internal.SWTEventListener;

public interface DrawListener extends SWTEventListener {

	/**
	 * Sent when a mouse button is pressed twice within the 
	 * (operating system specified) double click period.
	 *
	 * @param e an event containing information about the mouse double click
	 *
	 * @see org.eclipse.swt.widgets.Display#getDoubleClickTime()
	 */
	public void mouseDoubleClick(MouseEvent e);

	/**
	 * Sent when a mouse button is pressed.
	 *
	 * @param e an event containing information about the mouse button press
	 */
	public void mouseDown(MouseEvent e);

	/**
	 * Sent when a mouse button is released.
	 *
	 * @param e an event containing information about the mouse button release
	 */
	public void mouseUp(MouseEvent e);
	
	/**
	 * Sent when the mouse moves.
	 *
	 * @param e an event containing information about the mouse move
	 */
	public void mouseMove(MouseEvent e);
	
	/**
	 * Sent when a paint event occurs for the control.
	 *
	 * @param e an event containing information about the paint
	 */
	public void paintControl(PaintEvent e);
}
