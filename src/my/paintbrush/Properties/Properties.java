package my.paintbrush.Properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.widgets.Composite;

public abstract class Properties {
	
	public Property[] properties;
	
	public Properties(Property... properties) {
		this.properties = properties;
		//Update the current properties by the given properties
		//(containing the properties of the previous active tool)
		for (Property prop : getProperties()) {
			Object foundPropVal = getProperty(prop);
			if (foundPropVal != null)
				prop.value = foundPropVal;
		}
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
	
	public void print() {
		System.out.println("Properties from class: " + this.getClass().getSimpleName() + " {");
		for (Property prop : properties)
			System.out.println("\t" + prop.name + " = " + prop.value);
		System.out.println("}");
	}
	
	abstract public Property[] getProperties();
	
	public abstract class PropertiesComp extends Composite {
		public PropertiesComp(final Composite comp, int style) {
			super(comp, style);
		};
		
		public abstract Properties getCurProps();
	}
}
