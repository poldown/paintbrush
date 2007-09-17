package my.paintbrush.Properties;

import org.eclipse.swt.widgets.Composite;

public class EmptyProperties extends Properties {

	public EmptyProperties(Property... properties) {
		super(properties);
	}
	
	public EmptyProperties() {}
	
	public Property[] getProperties() {
		return new Property[] {}; 
	}
	
	public class EmptyPropertiesComp extends PropertiesComp {
		public EmptyPropertiesComp(Composite comp, int style) {
			super(comp, style);
		}
		
		public Properties getCurProps() {
			return new EmptyProperties(new Property[] {});
		}
	}
}
