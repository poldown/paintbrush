package my.paintbrush;

import my.paintbrush.controls.DrawingCanvas;
import my.paintbrush.controls.PbComposite;
import my.paintbrush.controls.ToolSelector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class SWTContent {

	public DrawingCanvas canvas;
	public ToolSelector toolSel;
	
	protected void createDrawingCanvas(Shell shell, int style) {
		PbComposite pbComposite = new PbComposite(shell, SWT.NONE, "Drawing Canvas");
		canvas = new DrawingCanvas(pbComposite, style, this);
		canvas.setLayoutData(new GridData(600, 600));
		GridData gridData = new GridData();
		gridData.verticalSpan = 4;
		pbComposite.setLayoutData(gridData);
	}
	
	protected void createToolSelector(Shell shell, int style) {
		PbComposite pbComposite = new PbComposite(shell, SWT.NONE, "Tool Selector");
		toolSel = new ToolSelector(pbComposite, style, shell);
		GridData gridData = new GridData(SWT.CENTER, SWT.TOP, true, true);
		pbComposite.setLayoutData(gridData);
	}
	
	protected SWTContent(final Shell shell) {
		shell.setLayout(new GridLayout(2, false));
		createDrawingCanvas(shell, SWT.BORDER);
		createToolSelector(shell, SWT.NONE);
	}
	
	public static void setDefaultMenu(Control control) {
		Menu menu = new Menu(control);
		MenuItem item = new MenuItem(menu, SWT.BOLD);
		item.setText("Undock");
		control.setMenu(menu);
		//...
		// TODO: Define the default menu
	}
}
