package my.paintbrush.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class PointerProperties extends EmptyProperties {

	public static final Property SELECTION_FCOLOR = new Property(
			"selection_fCOLOR", Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		
	public PointerProperties(Property... properties) {
		super(properties);
	}
	
	public PointerProperties() {}
	
	public Property[] getProperties() {
		return addProperties(super.getProperties(), SELECTION_FCOLOR); 
	}
	
	public class PointerPropertiesComp extends EmptyPropertiesComp {
		
		public PointerPropertiesComp(final Composite comp, int style) {
			super(comp, style);
			
			this.setLayout(new GridLayout(3, false));
			
			
		}
		
		public Properties getCurProps() {
			return new PointerProperties(
					addProperties(super.getCurProps().properties, SELECTION_FCOLOR));
		}
	}
}
