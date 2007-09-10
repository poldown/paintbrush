package my.paintbrush;

import org.eclipse.swt.graphics.Color;

public class Properties {

	public int width;
	public Color fColor, bColor;
	public int arcW, arcH;
	
	public Properties(int width, Color fColor, Color bColor, int arcW, int arcH) {
		this.width = width;
		this.fColor = fColor;
		this.bColor = bColor;
		this.arcW = arcW;
		this.arcH = arcH;
	}
}
