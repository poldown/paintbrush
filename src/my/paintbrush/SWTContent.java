package my.paintbrush;

import my.paintbrush.tools.ToolSelector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;

public class SWTContent {

	protected DrawingCanvas canvas;
	protected ToolSelector toolSel;
	
	protected void createDrawingCanvas(Shell shell, int style) {
		canvas = new DrawingCanvas(shell, style, this);
		GridData gridData = new GridData(300, 300);
		gridData.verticalSpan = 2;
		canvas.setLayoutData(gridData);
	}
	
	protected void createToolSelector(Shell shell, int style) {
		toolSel = new ToolSelector(shell, style);
		//toolSel.setSelection(new Point(1, 1));
	}
	
	protected SWTContent(final Shell shell) {
		shell.setLayout(new GridLayout(2, false));
		createDrawingCanvas(shell, SWT.BORDER);
		createToolSelector(shell, SWT.NONE);
		//createPropertiesComp(shell, SWT.BORDER);
	}
}
