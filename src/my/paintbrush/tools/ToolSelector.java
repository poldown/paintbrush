package my.paintbrush.tools;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class ToolSelector extends Composite {

	private Combo toolSel;
	
	public ToolSelector(Shell shell, int style) {
		super(shell, style);
		
		this.setLayout(new GridLayout(2, false));
		
		Label toolSelLabel = new Label(this, SWT.NONE);
		toolSelLabel.setText("Selected Tool:");
		toolSelLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		
		toolSel = new Combo(this, style);
		for (DrawingTool tool : DrawingTool.values())
			toolSel.add(tool.disName);
	}
	
	public DrawingTool getSelectedTool() {
		int index = toolSel.getSelectionIndex();
		if (index == -1)
			return DrawingTool.NONE;
		else
			return DrawingTool.values()[index];
	}
}
