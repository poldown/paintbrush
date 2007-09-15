package my.paintbrush.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.widgets.Composite;

public abstract class Properties {
	
	public Property[] properties;
	
	public Properties(Property... properties) {
		this.properties = properties;
	}
	
	public Properties() {}
	
	public Object getProperty(Property findProperty) {
		for (Property prop : properties)
			if (prop.name.equals(findProperty.name))
				return prop.value;
		return null;
	}
	
	public Property[] addProperties(Property[] to, Property... what) {
		List<Property> retProperties = new ArrayList<Property>(Arrays.asList(to));
		for (Property count : what)
			retProperties.add(count);
		return (Property[])(retProperties.toArray(new Property[0]));
	}
	
	abstract public Property[] getProperties();
	
	public abstract class PropertiesComp extends Composite {
		public PropertiesComp(final Composite comp, int style) {
			super(comp, style);
		};
		
		public abstract Properties getCurProps();
	}
}
