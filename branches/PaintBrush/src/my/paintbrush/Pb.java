package my.paintbrush;

import java.util.ArrayList;

import my.paintbrush.properties.PropertiesComp;
import my.paintbrush.tools.Circle;
import my.paintbrush.tools.DrawingObject;
import my.paintbrush.tools.DrawingTool;
import my.paintbrush.tools.Line;
import my.paintbrush.tools.Rectangle;
import my.paintbrush.tools.RoundRectangle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Pb {
	
	private Canvas canvas;
	private PropertiesComp propComp;
	
	private DrawingTool selTool = DrawingTool.RECTANGLE;
	private DrawingTool drawingTool = DrawingTool.NONE;
	
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
	
	public void createDrawingCanvas(Shell shell, int style) {
		canvas = new Canvas(shell, style);
		GridData gridData = new GridData(300, 300);
		gridData.verticalSpan = 2;
		canvas.setLayoutData(gridData);
	}
	
	public void createToolSelector(Shell shell, int style) {
		final Combo toolSel = new Combo(shell, style);
		for (DrawingTool tool : DrawingTool.values())
			toolSel.add(tool.disName);
		//toolSel.setSelection(new Point(1, 1));
		toolSel.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				selTool = DrawingTool.values()[toolSel.getSelectionIndex()];
			}
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
	}
	
	public void createPropertiesComp(Shell shell, int style) {
		propComp = new PropertiesComp(shell, style); 
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		propComp.setLayoutData(gridData);
	}
	
	public void createContents(final Shell shell) {
		shell.setLayout(new GridLayout(2, false));
		createDrawingCanvas(shell, SWT.BORDER);
		createToolSelector(shell, SWT.NONE);
		createPropertiesComp(shell, SWT.BORDER);
	}
	
	public void addDrawListener() {
		canvas.addMouseListener(new MouseListener() {
			public void mouseUp(MouseEvent e) {
				paintAll();
				drawingTool = DrawingTool.NONE;
			}
			public void mouseDown(MouseEvent e) {
				drawingTool = selTool;
				switch (selTool) {
				case LINE:
					drawingObjects.add(new Line(e.x, e.y, propComp.getCurProps()));
					break;
				case RECTANGLE:
					drawingObjects.add(new Rectangle(e.x, e.y, propComp.getCurProps()));
					break;
				case ROUNDRECTANGLE:
					drawingObjects.add(new RoundRectangle(e.x, e.y, propComp.getCurProps()));
					break;	
				case CIRCLE:
					drawingObjects.add(new Circle(e.x, e.y, propComp.getCurProps()));
					break;
				default:
					break;
				}
			}
			public void mouseDoubleClick(MouseEvent e) {}
		});
		
		canvas.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent e) {
				if (drawingTool != DrawingTool.NONE) {
					DrawingObject obj = drawingObjects.get(drawingObjects.size() - 1);
					obj.draw(canvas, e.x, e.y);
				}
			}
		});
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
