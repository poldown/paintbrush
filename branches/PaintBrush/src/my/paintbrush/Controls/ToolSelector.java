package my.paintbrush.Controls;

import java.lang.reflect.Constructor;

import my.paintbrush.Properties.Properties;
import my.paintbrush.Properties.Property;
import my.paintbrush.Properties.Properties.PropertiesComp;
import my.paintbrush.Tools.DrawingTool;

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
	private Properties propInstance;
	
	public ToolSelector(Composite comp, int style, Composite parent) {
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
				initiateTool(DrawingTool.values()
						[toolSel.getSelectionIndex()]);
			}
		});
		return toolSel;
	}
	
	public void initiateTool(DrawingTool tool) {
		if (tool != DrawingTool.NONE)
			createPropertiesComp(parent, SWT.BORDER, 
					tool.propertiesClassName);
		else
			if (propPbComp != null)
				propPbComp.dispose();
		this.pack();
		this.parent.pack();
		this.parent.layout();
	}
	
	private void setTool(DrawingTool tool) {
		toolSel.setText(tool.disName);
		initiateTool(tool);
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
			//Get the class of the (parent) Properties class
			Class<? extends Properties> prop = Class.forName(propClass).asSubclass(Properties.class);
			if (prop != null) {
				//Create the Properties class' instance
				if (propInstance != null && !propPbComp.isDisposed()) {
					//If an instance was created earlier - get the
					//current properties and create a new instance
					//using the special constructor with the Property[]
					//param (passing the current properties to the
					//constructor)
					Property[] props = propComp.getCurProps().properties;
					Constructor<? extends Properties> propCons = prop.getConstructor(Property[].class);
					propInstance = propCons.newInstance(new Object[] {props});
				} else
					//If no instance was created earlier - create it
					//with the default constructor (no need to pass
					//any current properties)
					propInstance = prop.newInstance();
				//Drill down and get the first subclass inside the
				//Properties class - this is the PropertiesComp class.
				//Then -> get its appropriate constructor.
				Constructor<? extends PropertiesComp> propCompCons = 
					prop.getClasses()[0].asSubclass(PropertiesComp.class).getConstructor(prop, Composite.class, Integer.TYPE);
				//If a properties composite is already visible, dispose
				if (propPbComp != null)
					propPbComp.dispose();
				//Create a new properties composite
				propPbComp = new PbComposite(comp, SWT.NONE, "Properties - " + getSelectedTool().disName);
				propComp = propCompCons.newInstance(propInstance, propPbComp, style); 
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
