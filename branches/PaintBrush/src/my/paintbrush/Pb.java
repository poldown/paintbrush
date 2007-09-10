package my.paintbrush;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Pb {

	public enum Drawing {
		NONE("<none>"),
		LINE("Line"),
		RECTANGLE("Rectangle"),
		ROUNDRECTANGLE("Round Rectangle"),
		CIRCLE("Circle");
		
		String disName;
		
		Drawing(String disName) {
			this.disName = disName;
		}
	};
	
	private Canvas canvas;
	
	private Canvas fColorSel;
	private Canvas bColorSel;
	private Spinner widthSel;
	
	private Drawing selTool = Drawing.RECTANGLE;
	private Drawing drawing = Drawing.NONE;
	
	java.util.List<DrawingObject> drawingObjects = new ArrayList<DrawingObject>();
	
	public Pb() {
		Display display = new Display();
		Shell shell = new Shell(display);
		createContents(shell);
		shell.open();
		
		addDrawListener();
		
		while (!shell.isDisposed())
			if (display.readAndDispatch())
				display.sleep();
		display.dispose();
	}
	
	public void createContents(final Shell shell) {
		Properties defaultProp = new Properties(3,
				new Color(Display.getCurrent(), 255, 0, 0),
				new Color(Display.getCurrent(), 0, 0, 255),
				30, 30);
		
		shell.setLayout(new GridLayout(2, false));
		canvas = new Canvas(shell, SWT.BORDER);
		GridData gridData = new GridData(300, 300);
		canvas.setLayoutData(gridData);
		
		Composite propertiesComp = new Composite(shell, SWT.BORDER);
		propertiesComp.setLayout(new GridLayout(1, false));
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		propertiesComp.setLayoutData(gridData);
		
		final Combo toolSel = new Combo(propertiesComp, SWT.NONE);
		for (Drawing tool : Drawing.values())
			toolSel.add(tool.disName);
		toolSel.setSelection(new Point(1, 1));
		toolSel.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				selTool = Drawing.values()[toolSel.getSelectionIndex()];
			}
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		widthSel = new Spinner(propertiesComp, SWT.NONE);
		widthSel.setMinimum(1);
		widthSel.setMaximum(50);
		widthSel.setSelection(defaultProp.width);
		
		MouseListener colorSelMouseListener = new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) {
				ColorDialog dialog = new ColorDialog(shell);
				dialog.setRGB(((Canvas)e.getSource()).getBackground().getRGB());
				RGB selRGB = dialog.open();
				if (selRGB != null)
					((Canvas)e.getSource()).setBackground(new Color(Display.getCurrent(), selRGB));
			}
		};
		
		fColorSel = new Canvas(propertiesComp, SWT.BORDER);
		fColorSel.setBackground(defaultProp.fColor);
		fColorSel.setLayoutData(new GridData(50, 50));
		fColorSel.addMouseListener(colorSelMouseListener);
		
		bColorSel = new Canvas(propertiesComp, SWT.BORDER);
		bColorSel.setBackground(defaultProp.bColor);
		bColorSel.setLayoutData(new GridData(50, 50));
		bColorSel.addMouseListener(colorSelMouseListener);
	}
	
	public void addDrawListener() {
		canvas.addMouseListener(new MouseListener() {
			public void mouseUp(MouseEvent e) {
				paintAll();
				drawing = Drawing.NONE;
			}
			public void mouseDown(MouseEvent e) {
				drawing = selTool;
				switch (selTool) {
				case LINE:
					drawingObjects.add(new Line(e.x, e.y, getCurProps()));
					break;
				case RECTANGLE:
					drawingObjects.add(new Rectangle(e.x, e.y, getCurProps()));
					break;
				case ROUNDRECTANGLE:
					drawingObjects.add(new RoundRectangle(e.x, e.y, getCurProps()));
					break;	
				case CIRCLE:
					drawingObjects.add(new Circle(e.x, e.y, getCurProps()));
					break;
				default:
					break;
				}
			}
			public void mouseDoubleClick(MouseEvent e) {}
		});
		
		canvas.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent e) {
				if (drawing != Drawing.NONE) {
					DrawingObject obj = drawingObjects.get(drawingObjects.size() - 1);
					obj.draw(canvas, e.x, e.y);
				}
			}
		});
	}
	
	public Properties getCurProps() {
		return new Properties(widthSel.getSelection(),
							  fColorSel.getBackground(),
							  bColorSel.getBackground(),
							  30,
							  30);
	}
	
	public void paintAll() {
		for (DrawingObject obj : drawingObjects)
			obj.draw(canvas, -1, -1);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Pb();
	}

}
