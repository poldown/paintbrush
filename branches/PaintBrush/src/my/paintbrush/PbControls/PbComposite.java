package my.paintbrush.PbControls;

import java.util.Arrays;
import java.util.List;

import my.paintbrush.SWTContent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public class PbComposite extends Composite {

	private Composite titleComp;
	private Canvas titleCanvas;
	private Label titleButton;
	private String title;
	
	private final Font titleFont = new Font(
			Display.getCurrent(), "Tahoma", 7, SWT.NONE);
	
	public PbComposite(Composite parent, int style, String title) {
		super(parent, style);
		
		this.title = title;
		this.setLayout(new GridLayout(1, true));
				
		titleComp = new Composite(this, SWT.NONE);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		titleComp.setLayoutData(gridData);
		titleComp.setLayout(new FormLayout());
		titleComp.setBackgroundMode(SWT.INHERIT_FORCE);
		
		titleCanvas = new Canvas(titleComp, SWT.NONE);
		titleCanvas.setBackgroundMode(SWT.INHERIT_FORCE);
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
		titleCanvas.setLayout(new FormLayout());
		
		titleButton = new Label(titleCanvas, SWT.NONE);
		titleButton.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
		
		final Image titleButtonRegularImage = getTitleButtonImage(SWT.COLOR_DARK_YELLOW);
		final Image titleButtonMouseOverImage = getTitleButtonImage(SWT.COLOR_GRAY);
		final Image titleButtonPressedImage = getTitleButtonImage(SWT.COLOR_YELLOW);
		
		titleButton.setImage(titleButtonPressedImage);
		data = new FormData();
		data.right = new FormAttachment(100, -5);
		data.top = new FormAttachment(0);
		data.bottom = new FormAttachment(100);
		titleButton.setLayoutData(data);
		/*titleButton.setSelection(true);
		titleButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setChildrenVisible(titleButton.getSelection());
			}
		});*/
		titleButton.setData("pressed", true);
		titleButton.addMouseTrackListener(new MouseTrackAdapter() {
			public void mouseExit(MouseEvent e) {
				if ((Boolean)titleButton.getData("pressed"))
					titleButton.setImage(titleButtonPressedImage);
				else
					titleButton.setImage(titleButtonRegularImage);
			}
			public void mouseEnter(MouseEvent e) {
				titleButton.setImage(titleButtonMouseOverImage);
			}
		});
		titleButton.addMouseListener(new MouseAdapter() {
			boolean pressing = false;
			boolean pressed = (Boolean)titleButton.getData("pressed");
			public void mouseUp(MouseEvent e) {
				if (pressing) {
					pressing = false;
					pressed = !pressed;
					setChildrenVisible(pressed);
					titleButton.setData("pressed", pressed);
				}
			}
			public void mouseDown(MouseEvent e) {
				pressing = true;
				if (pressed)
					titleButton.setImage(titleButtonRegularImage);
				else
					titleButton.setImage(titleButtonPressedImage);
			}
		});
		
		SWTContent.setDefaultMenu(this);
		SWTContent.setDefaultMenu(titleComp);
		SWTContent.setDefaultMenu(titleCanvas);
		SWTContent.setDefaultMenu(titleButton);
	}
	
	private Image getTitleButtonImage(int color) {
		ImageData imageData = new ImageData(8, 8, 8, new PaletteData(0, 0, 0));
		imageData.transparentPixel = 0;
		Image image = new Image(Display.getCurrent(), imageData);
		GC gc = new GC(image);
		gc.setForeground(Display.getCurrent().getSystemColor(color));
		gc.setLineWidth(2);
		//gc.drawLine(1, 1, 7, 7);
		//gc.drawLine(7, 1, 1, 7);
		gc.drawRectangle(1, 1, 6, 6);
		gc.dispose();
		return image;
	}

	protected void setChildrenVisible(boolean visible) {
		List<Control> toIgnore = Arrays.asList((Control)titleComp);
		for (Control control : this.getChildren())
			if (!toIgnore.contains(control))
				control.setVisible(visible);
	}

	private void updateCanvas(GC gc) {
		final Display display = Display.getCurrent();
		gc.setBackgroundPattern(
				new Pattern(display, 0, 0, 
						titleCanvas.getSize().x,
						titleCanvas.getSize().y,
						display.getSystemColor(SWT.COLOR_BLACK),
						display.getSystemColor(SWT.COLOR_BLUE)));
		gc.fillRoundRectangle(0, 0, titleCanvas.getSize().x, 
							   titleCanvas.getSize().y, 10, 10);
		gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		gc.setFont(titleFont);
		gc.drawText(title, 5, 2);
	}
}
