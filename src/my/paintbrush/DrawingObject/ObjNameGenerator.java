package my.paintbrush.DrawingObject;

import java.util.HashMap;
import java.util.Map;

public class ObjNameGenerator {

	Map<DrawingTool, Integer> counter;
	
	public ObjNameGenerator() {
		counter = new HashMap<DrawingTool, Integer>();
		for (DrawingTool tool : DrawingTool.values())
			counter.put(tool, 0);
	}
	
	public String getObjNameForTool(DrawingTool tool) {
		counter.put(tool, counter.get(tool) + 1);
		return tool.disName + " " + counter.get(tool);
	}
	
}
