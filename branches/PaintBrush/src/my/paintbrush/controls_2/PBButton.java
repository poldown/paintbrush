package my.paintbrush.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public class PBButton extends Canvas {

	Label label;
	
	public PBButton (Composite parent, int style) {
		super (parent, style);
		
		label = new Label(this, SWT.NONE);
		label.setLocation(6, 2);
		label.setBackground(null);
		label.addMouseTrackListener(new MouseTrackListener() {
		
			public void mouseHover(MouseEvent e) {
				
			}
		
			public void mouseExit(MouseEvent e) {
				FontData[] pbfd = label.getFont().getFontData();
				for (FontData fd : pbfd)
					fd.setStyle(SWT.NONE);
				label.setFont(new Font(Display.getCurrent(), pbfd));
				layout();
			}
		
			public void mouseEnter(MouseEvent e) {
				FontData[] pbfd = label.getFont().getFontData();
				for (FontData fd : pbfd)
					fd.setStyle(SWT.BOLD);
				label.setFont(new Font(Display.getCurrent(), pbfd));
				layout();
			}
		
		});
	}
	
	public void setText(String text) {
		label.setText(text);
		layout();
	}
	
	@Override
	public void layout() {
		label.pack();
		this.pack();
		onRePaint();
	}
	
	@Override
	public void pack() {
		checkWidget ();
		setSize (computeSize (label.getSize().x + 8, label.getSize().y + 6, true));
	}
	
	
	private void onRePaint() {
		GC gc = new GC(this);
		int x1 = 1; int y1 = 1;
		int x2 = this.getSize().x - 1; int y2 = this.getSize().y - 1;
		System.out.println("x1: " + x1 + " y1: " + y1 + " x2: " + x2 + " y2: " + y2);
		gc.setLineWidth(2);
		gc.setForeground(new Color(Display.getCurrent(), 192, 192, 192));
		gc.drawLine(x1, y1, x2, y1);
		gc.drawLine(x1, y1, x1, y2);
		gc.setForeground(new Color(Display.getCurrent(), 32, 32, 32));
		gc.drawLine(x1, y2, x2, y2);
		gc.drawLine(x2, y1, x2, y2);
		gc.dispose();
	}
}
