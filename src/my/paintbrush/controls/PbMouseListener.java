package my.paintbrush.controls;

import org.eclipse.swt.events.MouseEvent;

public abstract class PbMouseListener {

	private boolean listening = true;
	
	public abstract void mouseUp(MouseEvent e);
	
	public abstract void mouseDown(MouseEvent e);
	
	public abstract void mouseDoubleClick(MouseEvent e);
	
	public boolean isListening() {
		return listening;
	}
	
	public void stopListening() {
		listening = false;
	}
}
