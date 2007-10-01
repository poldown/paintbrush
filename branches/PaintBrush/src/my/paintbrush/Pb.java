package my.paintbrush;

import java.lang.reflect.Constructor;

import my.paintbrush.Controls.PbComposite;
import my.paintbrush.Properties.Properties;
import my.paintbrush.Properties.Property;
import my.paintbrush.Properties.Properties.PropertiesComp;
import my.paintbrush.Tools.DrawingTool;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.*;

public class Pb {
	
	public static int ToolSelectedEvent = 1000;
	public static int PropChangeEvent = 1001;
	
	@SuppressWarnings("unused")
	private SWTContent swt;
	private Shell shell;
	
	private PbComposite propPbComp;
	private Properties propInstance;
	
	public void initiateTool(DrawingTool tool) {
		if (tool != DrawingTool.NONE)
			createPropertiesComp(shell, SWT.BORDER, tool);
		else
			if (propPbComp != null)
				propPbComp.dispose();
		/*this.pack();
		this.parent.pack();
		this.parent.layout();*/
		shell.pack();
		shell.layout();
	}
	
	protected void createPropertiesComp(Composite comp, int style, DrawingTool tool) {
		try {
			//Get the class of the (parent) Properties class
			Class<? extends Properties> prop = Class.forName(tool.propertiesClassName).asSubclass(Properties.class);
			if (prop != null) {
				//Create the Properties class' instance
				if (propInstance != null && !propPbComp.isDisposed()) {
					//If an instance was created earlier - get the
					//current properties and create a new instance
					//using the special constructor with the Property[]
					//param (passing the current properties to the
					//constructor)
					Property[] props = swt.propComp.getCurProps().properties;
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
				propPbComp = new PbComposite(comp, SWT.NONE, "Properties - " + tool.disName);
				swt.propComp = propCompCons.newInstance(propInstance, propPbComp, style);
				swt.propComp.addListener(Pb.PropChangeEvent, handlePropChangeEvent(tool));
				GridData gridData = new GridData(SWT.CENTER, SWT.TOP, false, true);
				propPbComp.setLayoutData(gridData);
				swt.propComp.pack();
				propPbComp.pack();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Listener handlePropChangeEvent(final DrawingTool drawingTool) {
		return new Listener() {
			public void handleEvent(Event event) {
				event.data = drawingTool;
				notifyPropChange(event);
			}
		};
	}
	
	private void notifyPropChange(Event event) {
		System.out.println("Delegating properties change event handling...");
		event.item = event.widget;
		shell.notifyListeners(PropChangeEvent, event);
	}
	
	private Listener handleToolSelectionEvent() {
		return new Listener() {
			public void handleEvent(Event event) {
				System.out.println("Tool Selected! Initiating...");
				initiateTool((DrawingTool) event.data);
				notifyPropChange(event);
			}
		};
	}
	
	public Pb() {
		Display display = new Display();
		shell = new Shell(display);
		shell.addListener(ToolSelectedEvent, handleToolSelectionEvent());
		swt = new SWTContent(shell);
		shell.pack();
		shell.open();
		
		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
		display.dispose();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Pb();
	}

}
