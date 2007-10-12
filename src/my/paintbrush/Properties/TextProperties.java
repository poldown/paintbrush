package my.paintbrush.Properties;

import my.paintbrush.PbSWT;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Spinner;

public class TextProperties extends EmptyProperties {
	public static final Property FCOLOR = new Property(
			"fCOLOR", Display.getCurrent().getSystemColor(SWT.COLOR_RED));
	public static final Property FCOLOR_TRANS = new Property(
			"fColor_Trans", 255);
	public static final Property TEXT = new Property("Text",
			"<Enter text>");
	public static final Property FONT = new Property("Font",
			new Font(Display.getCurrent(), "Arial", 0, PbSWT.NONE));
	
	public TextProperties(Property... properties) {
		super(properties);
	}
	
	public TextProperties() {}
	
	@Override
	public Property[] getProperties() {
		return addProperties(super.getProperties(), FCOLOR, FCOLOR_TRANS, TEXT, FONT);
	}
	
	public class TextPropertiesComp extends EmptyPropertiesComp {
		
		protected Canvas fColorSel;
		protected Scale fColor_Transparent;
		
		public TextPropertiesComp(final Composite comp, int style) {
			super(comp, style);
			
			this.setLayout(new GridLayout(3, false));
			
			fColorSel = addColorSel("Foreground Color:", (Color)FCOLOR.value);
			fColor_Transparent = addColorTransSel(fColorSel, (Integer)FCOLOR_TRANS.value);
			//TODO: Add textSel & fontSel, Change Properties for the text tool to these properties class.
		}
		
		@Override
		public Properties getCurProps() {
			return new RoundRectangleProperties(
					addProperties(super.getCurProps().properties,
							FCOLOR.newWithValue(fColorSel.getBackground()),
							FCOLOR_TRANS.newWithValue(fColor_Transparent.getSelection()),
							//TODO: Update!
							TEXT.newWithValue(textSel.getSelection()),
							FONT.newWithValue(fontSel.getSelection())));
		}
	}
}
