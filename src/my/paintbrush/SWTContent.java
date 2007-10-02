package my.paintbrush;

import my.paintbrush.PbControls.PbComposite;
import my.paintbrush.PbControls.PbDrawingCanvas;
import my.paintbrush.PbControls.PbSampleView;
import my.paintbrush.PbControls.PbToolSelector;
import my.paintbrush.Properties.Properties.PropertiesComp;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class SWTContent {

	public PbDrawingCanvas canvas;
	public PbToolSelector toolSel;
	public PbSampleView sampleView;
	
	public PropertiesComp propComp;
	
	protected void createDrawingCanvas(Shell shell, int style) {
		PbComposite pbComposite = new PbComposite(shell, SWT.NONE, "Drawing Canvas");
		canvas = new PbDrawingCanvas(pbComposite, style, this);
		canvas.setLayoutData(new GridData(600, 600));
		GridData gridData = new GridData();
		gridData.verticalSpan = 4;
		pbComposite.setLayoutData(gridData);
	}
	
	protected void createToolSelector(Shell shell, int style) {
		PbComposite pbComposite = new PbComposite(shell, SWT.NONE, "Tool Selector");
		toolSel = new PbToolSelector(pbComposite, style, shell);
		GridData gridData = new GridData(SWT.CENTER, SWT.TOP, true, true);
		pbComposite.setLayoutData(gridData);
	}
	
	protected void createSampleView(Shell shell, int style) {
		PbComposite pbComposite = new PbComposite(shell, SWT.NONE, "Sample View");
		sampleView = new PbSampleView(pbComposite, style, shell, this);
		GridData gridData = new GridData(SWT.CENTER, SWT.TOP, true, true);
		pbComposite.setLayoutData(gridData);
	}
	
	protected SWTContent(final Shell shell) {
		shell.setLayout(new GridLayout(2, false));
		createDrawingCanvas(shell, SWT.BORDER);
		createToolSelector(shell, SWT.NONE);
		createSampleView(shell, SWT.BORDER);
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
