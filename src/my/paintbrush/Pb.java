package my.paintbrush;

import java.lang.reflect.Constructor;

import my.paintbrush.DrawingObject.DrawingTool;
import my.paintbrush.Events.PbControlEvent;
import my.paintbrush.Listeners.PbControlsAdapter;
import my.paintbrush.Listeners.PbControlsListener;
import my.paintbrush.Listeners.PbTypedListener;
import my.paintbrush.PbControls.PbComposite;
import my.paintbrush.Properties.Properties;
import my.paintbrush.Properties.Property;
import my.paintbrush.Properties.Properties.PropertiesComp;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Pb {
	
	private static final DrawingTool defaultTool = DrawingTool.POINTER;
	
	private SWTContent swt;
	private Shell shell;
	
	private PbComposite propPbComp;
	private Properties propInstance;
	
	protected PbTypedListener controlsListener = new PbTypedListener(
			new PbControlsListener() {
				public void toolSelected(PbControlEvent e) {
					System.out.println("Tool Selected! Initiating...");
					initiateTool((DrawingTool) e.tool);
					notifyPropChange(e);
				}
				public void propertiesChanged(PbControlEvent e) {
					System.out.println("Properties Changed (" + e.item + ")");
					// The drawingTool which is being used must be supplied
					// as the e.tool!
					swt.sampleView.updateSampleView(e.tool);
				}
			});
	
	public void initiateTool(DrawingTool tool) {
		if (tool != DrawingTool.NONE)
			createPropertiesComp(shell, SWT.BORDER, tool);
		else
			if (propPbComp != null)
				propPbComp.dispose();
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
				swt.propComp.addListener(PbSWT.PropertiesChanged, handlePropChangeEvent(tool));
				GridData gridData = new GridData(SWT.CENTER, SWT.TOP, false, true);
				propPbComp.setLayoutData(gridData);
				swt.propComp.pack();
				propPbComp.pack();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private PbTypedListener handlePropChangeEvent(final DrawingTool drawingTool) {
		return new PbTypedListener(new PbControlsAdapter() {
			public void propertiesChanged(PbControlEvent e) {
				e.tool = drawingTool;
				notifyPropChange(e);
			}
		});
	}
	
	private void notifyPropChange(PbControlEvent event) {
		System.out.println("Delegating properties change event handling...");
		//event.item = event.widget;
		shell.notifyListeners(PbSWT.PropertiesChanged, event);
	}
	
	public Pb() {
		Display display = new Display();
		shell = new Shell(display);
		swt = new SWTContent(shell);
		shell.addListener(PbSWT.ToolSelected, controlsListener);
		shell.addListener(PbSWT.PropertiesChanged, controlsListener);
		shell.pack();
		shell.open();
		
		selectDefaultTool();
		
		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
		display.dispose();
	}

	private void selectDefaultTool() {
		swt.toolSel.selectTool(defaultTool);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Pb();
	}

}
