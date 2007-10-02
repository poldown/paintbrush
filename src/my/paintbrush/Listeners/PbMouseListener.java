package my.paintbrush.Listeners;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;

public abstract class PbMouseListener implements MouseListener {

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
