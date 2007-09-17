package my.paintbrush.controls;

import my.paintbrush.SWTContent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class PbComposite extends Composite {

	private Canvas titleCanvas;
	private String title;
	
	boolean mouseOver = false;
	
	public PbComposite(Composite parent, int style, String title) {
		super(parent, style);
		
		this.title = title;
		this.setLayout(new GridLayout(1, true));
				
		final Composite titleComp = new Composite(this, SWT.NONE);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		titleComp.setLayoutData(gridData);
		titleComp.setLayout(new FormLayout());
		titleComp.setBackgroundMode(SWT.INHERIT_FORCE);
		
		/*Button button = new Button(titleComp, SWT.FLAT);
		ImageData imageData = new ImageData(8, 8, 8, new PaletteData(0, 0, 0));
		Image image = new Image(Display.getCurrent(), imageData);
		GC gc = new GC(image);
		gc.setBackground(button.getBackground());
		gc.fillRectangle(0, 0, 8, 8);
		gc.setForeground(new Color(Display.getCurrent(), 0, 0, 0));
		gc.setLineWidth(3);
		gc.drawLine(1, 1, 7, 7);
		gc.drawLine(7, 1, 1, 7);
		gc.dispose();
		button.setImage(image);
		FormData data = new FormData();
		data.height = 20;
		data.right = new FormAttachment(100);
		data.top = new FormAttachment(0);
		data.bottom = new FormAttachment(100);
		button.setLayoutData(data);*/
		
		titleCanvas = new Canvas(titleComp, SWT.NONE);
		FormData data = new FormData();
		data.height = 15;
		data.width = new GC(titleCanvas).textExtent(title).x + 10;
		data.left = new FormAttachment(0);
		data.right = new FormAttachment(100);
		data.top = new FormAttachment(0);
		data.bottom = new FormAttachment(100);
		titleCanvas.setLayoutData(data);
		titleCanvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				updateCanvas(e.gc);
			}
		});
		
		SWTContent.setDefaultMenu(this);
		SWTContent.setDefaultMenu(titleComp);
		SWTContent.setDefaultMenu(titleCanvas);
	}
	
	private void updateCanvas(GC gc) {
		final Display display = Display.getCurrent();
		gc.setBackgroundPattern(
				new Pattern(display, 0, 0, 
						titleCanvas.getSize().x,
						titleCanvas.getSize().y,
						display.getSystemColor(SWT.COLOR_BLUE),
						display.getSystemColor(SWT.COLOR_BLACK)));
		gc.fillRoundRectangle(0, 0, titleCanvas.getSize().x, 
							   titleCanvas.getSize().y, 10, 10);
		gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		gc.setFont(new Font(display, "Tahoma", 7, SWT.NONE));
		gc.drawText(title, 5, 2);
	}
}
