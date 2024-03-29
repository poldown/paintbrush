package my.paintbrush.DrawingObject;

import java.lang.reflect.Constructor;

import my.paintbrush.Properties.Properties;
import my.paintbrush.Properties.Property;

public enum DrawingTool {
	NONE("<none>", "", "my.paintbrush.Properties.EmptyProperties"),
	POINTER("Pointer", "my.paintbrush.Tools.Pointer", "my.paintbrush.Properties.PointerProperties"),
	LINE("Line", "my.paintbrush.Tools.Line", "my.paintbrush.Properties.BasicProperties"),
	//ARC("Arc", "my.paintbrush.Tools.Arc", "my.paintbrush.Properties.BasicProperties"),
	RECTANGLE("Rectangle", "my.paintbrush.Tools.Rectangle", "my.paintbrush.Properties.SimpleProperties"),
	ROUNDRECTANGLE("Round Rectangle", "my.paintbrush.Tools.RoundRectangle", "my.paintbrush.Properties.RoundRectangleProperties"),
	ELLIPSE("Ellipse", "my.paintbrush.Tools.Ellipse", "my.paintbrush.Properties.SimpleProperties"),
	FREEDRAW("Free Drawing", "my.paintbrush.Tools.FreeDraw", "my.paintbrush.Properties.BasicProperties"),
	SHAPES("Shapes", "my.paintbrush.Tools.Shapes", "my.paintbrush.Properties.ShapesProperties"),
	FREEPOLYGON("Free Polygon", "my.paintbrush.Tools.FreePolygon", "my.paintbrush.Properties.SimpleProperties"),
	TEXT("Text", "my.paintbrush.Tools.Text", "my.paintbrush.Properties.TextProperties");
	
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
	
	public Constructor<? extends DrawingObject> getCorrespondingDOCons() throws Exception {
		Class<? extends DrawingObject> toolClass = Class.forName(this.className).asSubclass(DrawingObject.class);
		Constructor<? extends DrawingObject> cons = toolClass.getConstructor(
				Integer.TYPE,
				Integer.TYPE,
				Class.forName(this.propertiesClassName));
		return cons;
	}
	
	public Constructor<? extends DrawingObject> getCorrespondingDODefCons() throws Exception {
		Class<? extends DrawingObject> toolClass = Class.forName(this.className).asSubclass(DrawingObject.class);
		Constructor<? extends DrawingObject> cons = toolClass.getConstructor();
		return cons;
	}
}
