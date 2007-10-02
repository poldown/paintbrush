package my.paintbrush.PbControls;

import my.paintbrush.PbSWT;
import my.paintbrush.Events.PbControlEvent;
import my.paintbrush.Tools.DrawingTool;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class PbToolSelector extends Composite {

	final DrawingTool defaultTool = DrawingTool.NONE;
	
	private Composite parent;
	
	private Combo toolSel;
	
	public PbToolSelector(Composite comp, int style, Composite parent) {
		super(comp, style);
		
		this.parent = parent;
		this.setLayout(new GridLayout(2, false));
		
		toolSel = addToolSel(style);
		setTool(defaultTool);
	}
	
	private Combo addToolSel(int style) {
		Label toolSelLabel = new Label(this, SWT.NONE);
		toolSelLabel.setText("Selected Tool:");
		toolSelLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		
		final Combo toolSel = new Combo(this, style);
		for (DrawingTool tool : DrawingTool.values())
			toolSel.add(tool.disName);
		
		toolSel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				selectTool(DrawingTool.values()
						[toolSel.getSelectionIndex()]);
			}
		});
		return toolSel;
	}
	
	private void setTool(DrawingTool tool) {
		toolSel.setText(tool.disName);
		selectTool(tool);
	}
	
	private void selectTool(DrawingTool tool) {
		PbControlEvent event = new PbControlEvent();
		event.tool = tool;
		parent.notifyListeners(PbSWT.ToolSelected, event);
	}

	public DrawingTool getSelectedTool() {
		int index = toolSel.getSelectionIndex();
		if (index == -1)
			return DrawingTool.NONE;
		else
			return DrawingTool.values()[index];
	}
}
