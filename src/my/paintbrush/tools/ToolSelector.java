package my.paintbrush.tools;

import java.lang.reflect.Constructor;

import my.paintbrush.properties.Properties;
import my.paintbrush.properties.Properties.PropertiesComp;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class ToolSelector extends Composite {

	final DrawingTool defaultTool = DrawingTool.NONE;
	
	private Combo toolSel;
	public PropertiesComp propComp;
	
	public ToolSelector(Shell shell, int style) {
		super(shell, style);
		
		this.setLayout(new GridLayout(2, false));
		
		Label toolSelLabel = new Label(this, SWT.NONE);
		toolSelLabel.setText("Selected Tool:");
		toolSelLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		
		toolSel = new Combo(this, style);
		for (DrawingTool tool : DrawingTool.values())
			toolSel.add(tool.disName);
		
		toolSel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				instatateTool(DrawingTool.values()
						[toolSel.getSelectionIndex()]);
			}
		});
		
		toolSel.setText(defaultTool.disName);
		instatateTool(defaultTool);
	}
	
	public void instatateTool(DrawingTool tool) {
		if (propComp != null)
			propComp.dispose();
		if (tool != DrawingTool.NONE)
			createPropertiesComp(this, SWT.BORDER, 
					tool.propertiesClassName);
		this.pack();
		this.getParent().pack();
	}
	
	public DrawingTool getSelectedTool() {
		int index = toolSel.getSelectionIndex();
		if (index == -1)
			return DrawingTool.NONE;
		else
			return DrawingTool.values()[index];
	}
	
	protected void createPropertiesComp(Composite comp, int style, String propClass) {
		try {
			Class<? extends Properties> prop = Class.forName(propClass).asSubclass(Properties.class);
			if (prop != null) {
				Constructor<? extends PropertiesComp> cons = 
					prop.getClasses()[0].asSubclass(PropertiesComp.class).getConstructor(prop, Composite.class, Integer.TYPE);
				propComp = cons.newInstance(prop.newInstance(), comp, style); 
				GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
				propComp.setLayoutData(gridData);
				propComp.pack();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
