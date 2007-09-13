package my.paintbrush.controls;

import java.lang.reflect.Constructor;

import my.paintbrush.properties.Properties;
import my.paintbrush.properties.Properties.PropertiesComp;
import my.paintbrush.tools.DrawingTool;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ToolSelector extends Composite {

	final DrawingTool defaultTool = DrawingTool.NONE;
	
	private Composite parent;
	
	private Combo toolSel;
	private PbComposite propPbComp;
	public PropertiesComp propComp;
	
	public ToolSelector(Composite comp, int style, Composite parent) {
		super(comp, style);
		
		this.parent = parent;
		this.setLayout(new GridLayout(2, false));
		
		Label toolSelLabel = new Label(this, SWT.NONE);
		toolSelLabel.setText("Selected Tool:");
		toolSelLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		
		toolSel = new Combo(this, style);
		for (DrawingTool tool : DrawingTool.values())
			toolSel.add(tool.disName);
		
		toolSel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				initiateTool(DrawingTool.values()
						[toolSel.getSelectionIndex()]);
			}
		});
		
		toolSel.setText(defaultTool.disName);
		initiateTool(defaultTool);
	}
	
	public void initiateTool(DrawingTool tool) {
		if (propPbComp != null)
			propPbComp.dispose();
		if (tool != DrawingTool.NONE)
			createPropertiesComp(parent, SWT.BORDER, 
					tool.propertiesClassName);
		this.pack();
		this.parent.pack();
		this.parent.layout();
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
				propPbComp = new PbComposite(comp, SWT.NONE, "Properties");
				propComp = cons.newInstance(prop.newInstance(), propPbComp, style); 
				GridData gridData = new GridData(SWT.CENTER, SWT.TOP, false, true);
				propPbComp.setLayoutData(gridData);
				propComp.pack();
				propPbComp.pack();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
