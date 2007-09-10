package my.paintbrush.tools;

public enum DrawingTool {
	NONE("<none>"),
	LINE("Line"),
	RECTANGLE("Rectangle"),
	ROUNDRECTANGLE("Round Rectangle"),
	CIRCLE("Circle");
	
	public String disName;
	
	DrawingTool(String disName) {
		this.disName = disName;
	}
}
