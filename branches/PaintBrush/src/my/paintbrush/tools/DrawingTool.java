package my.paintbrush.tools;

public enum DrawingTool {
	NONE("<none>", ""),
	LINE("Line", "my.paintbrush.tools.Line"),
	RECTANGLE("Rectangle", "my.paintbrush.tools.Rectangle"),
	ROUNDRECTANGLE("Round Rectangle", "my.paintbrush.tools.RoundRectangle"),
	ELLIPSE("Ellipse", "my.paintbrush.tools.Ellipse"),
	FREEDRAW("Free Drawing", "my.paintbrush.tools.FreeDraw"),
	SHAPES("Shapes", "my.paintbrush.tools.Shapes");
	
	public String disName;
	public String className;
	
	DrawingTool(String disName, String className) {
		this.disName = disName;
		this.className = className;
	}
}
