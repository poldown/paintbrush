package my.paintbrush.tools;

import java.lang.reflect.Constructor;

import my.paintbrush.properties.Properties;
import my.paintbrush.properties.Property;

public enum DrawingTool {
	NONE("<none>", "", "my.paintbrush.properties.Properties"),
	LINE("Line", "my.paintbrush.tools.Line", "my.paintbrush.properties.BasicProperties"),
	//ARC("Arc", "my.paintbrush.tools.Arc", "my.paintbrush.properties.BasicProperties"),
	RECTANGLE("Rectangle", "my.paintbrush.tools.Rectangle", "my.paintbrush.properties.SimpleProperties"),
	ROUNDRECTANGLE("Round Rectangle", "my.paintbrush.tools.RoundRectangle", "my.paintbrush.properties.RoundRectangleProperties"),
	ELLIPSE("Ellipse", "my.paintbrush.tools.Ellipse", "my.paintbrush.properties.SimpleProperties"),
	FREEDRAW("Free Drawing", "my.paintbrush.tools.FreeDraw", "my.paintbrush.properties.BasicProperties"),
	SHAPES("Shapes", "my.paintbrush.tools.Shapes", "my.paintbrush.properties.ShapesProperties"),
	FREEPOLYGON("Free Polygon", "my.paintbrush.tools.FreePolygon", "my.paintbrush.properties.SimpleProperties");
	
	public String disName;
	public String className;
	public String propertiesClassName;
	
	DrawingTool(String disName, String className, String propertiesClassName) {
		this.disName = disName;
		this.className = className;
		this.propertiesClassName = propertiesClassName;
	}
	
	public Property[] getProperties() {
		try {
			Class<? extends Properties> propClass = Class.forName(this.propertiesClassName).asSubclass(Properties.class);
			Constructor<? extends Properties> cons = propClass.getConstructor();
			Properties prop = cons.newInstance();
			Property[] properties = prop.getProperties();
			return properties;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
