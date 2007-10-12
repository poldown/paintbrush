package my.paintbrush.Tools;

import my.paintbrush.PbSWT;
import my.paintbrush.DrawingObject.EmptyDrawingObject;
import my.paintbrush.PbControls.PbDrawable;
import my.paintbrush.Properties.TextProperties;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

public class Text extends EmptyDrawingObject {

	protected Color fColor;
	protected int fColor_Trans;
	protected String text;
	protected Font font;

	private int height;
	
	public Text(int x0, int y0, TextProperties prop) {
		super(x0, y0, prop);
		fColor = (Color)prop.getProperty(TextProperties.FCOLOR);
		fColor_Trans = (Integer)prop.getProperty(TextProperties.FCOLOR_TRANS);
		text = (String)prop.getProperty(TextProperties.TEXT);
		font = (Font)prop.getProperty(TextProperties.FONT);
	}
	
	public void draw(Drawable drawable, int x1, int y1) {
		GC gc = new GC(drawable);
		if (x1 != -1 && y1 != -1)
			height = Math.abs(y1 - y0);
		updateFontHeight(gc);
		gc.setFont(font);
		gc.setTextAntialias(PbSWT.OFF);
		gc.setAlpha(fColor_Trans);
		//gc.setBackground(bColor);
		gc.setForeground(fColor);
		if (x1 != -1 && y1 != -1) {
			//Recalculate height using the current text extent
			height /= ((float)gc.textExtent(text).y) / (float)height;
			updateFontHeight(gc);
			if (x0 <= x1)
				this.x1 = x0 + gc.textExtent(text).x;
			else
				this.x1 = x0 - gc.textExtent(text).x;
			this.y1 = y1;
		}
		if (height != 0)
			gc.drawText(text, Math.min(x0, this.x1), Math.min(y0, this.y1),
						PbSWT.DRAW_DELIMITER | PbSWT.DRAW_TRANSPARENT);
		//System.out.println(height == 0?"N/A":(((float)gc.textExtent(text).y) / (float)height));
		gc.dispose();
	}

	private void updateFontHeight(GC gc) {
		FontData fontData = font.getFontData()[0];
		fontData.setHeight(height);
		font = new Font(Display.getCurrent(), fontData);
		gc.setFont(font);
	}

	public void drawSample(PbDrawable drawable) {
		GC gc = new GC(drawable);
		text = "ab";
		height = 16;
		updateFontHeight(gc);
		x0 = (drawable.width - gc.textExtent(text).x) / 2;
		y0 = (drawable.height - gc.textExtent(text).y) / 2;
		x1 = (drawable.width + gc.textExtent(text).x) / 2;
		y1 = (drawable.height + gc.textExtent(text).y) / 2;
		gc.dispose();
		draw(drawable, -1, -1);
	}

	public String getInstructions() {
		return "Inserts Text";
	}

}
