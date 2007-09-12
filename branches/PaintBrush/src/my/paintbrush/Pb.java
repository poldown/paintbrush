package my.paintbrush;

import org.eclipse.swt.widgets.*;

public class Pb {
	
	private SWTContent swt;
	
	public Pb() {
		Display display = new Display();
		Shell shell = new Shell(display);
		swt = new SWTContent(shell);
		shell.pack(true);
		shell.open();
		
		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
		display.dispose();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Pb();
	}

}
