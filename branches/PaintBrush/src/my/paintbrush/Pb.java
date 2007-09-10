package my.paintbrush;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import my.paintbrush.tools.DrawingObject;
import my.paintbrush.tools.DrawingTool;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.widgets.*;

public class Pb {
	
	private SWTContent swt;
	
	private DrawingTool drawingTool = DrawingTool.NONE;
	
	java.util.List<DrawingObject> drawingObjects = new ArrayList<DrawingObject>();
	
	public Pb() {
		Display display = new Display();
		Shell shell = new Shell(display);
		swt = new SWTContent(shell);
		shell.pack(true);
		shell.open();
		
		addDrawListener(swt.canvas);
		
		while (!shell.isDisposed())
			if (display.readAndDispatch())
				display.sleep();
		display.dispose();
	}
	
	public void addDrawListener(final Canvas canvas) {
		canvas.addMouseListener(new MouseListener() {
			public void mouseUp(MouseEvent e) {
				paintAll();
				drawingTool = DrawingTool.NONE;
			}
			public void mouseDown(MouseEvent e) {
				drawingTool = swt.toolSel.getSelectedTool();
				if (drawingTool != DrawingTool.NONE) {
					try {
						Class tool = Class.forName(drawingTool.className);
						@SuppressWarnings("unchecked")
						Constructor<DrawingObject> cons = tool.getConstructor(new Class[] {
								Integer.TYPE,
								Integer.TYPE,
								my.paintbrush.properties.Properties.class
						});
						drawingObjects.add(cons.newInstance(new Object[] {
								e.x, e.y, swt.propComp.getCurProps()
						}));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
			public void mouseDoubleClick(MouseEvent e) {}
		});
		
		canvas.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent e) {
				if (drawingTool != DrawingTool.NONE && 
						drawingObjects.size() > 0) {
					DrawingObject obj = drawingObjects.get(drawingObjects.size() - 1);
					obj.draw(canvas, e.x, e.y);
				}
			}
		});
	}
	
	public void paintAll() {
		for (DrawingObject obj : drawingObjects)
			obj.draw(swt.canvas, -1, -1);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Pb();
	}

}
