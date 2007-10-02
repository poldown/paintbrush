package my.paintbrush.Listeners;

import my.paintbrush.Events.PbControlEvent;

import org.eclipse.swt.internal.SWTEventListener;

public interface PbControlsListener extends SWTEventListener {

	public void propertiesChanged(PbControlEvent e);
	
	public void toolSelected(PbControlEvent e);
	
}
