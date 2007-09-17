package my.paintbrush.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class SimpleProperties extends BasicProperties {

	public static final Property BCOLOR = new Property(
			"bColor", Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
	
	public SimpleProperties(Property... properties) {
		super(properties);
	}
	
	public SimpleProperties() {}
	
	public Property[] getProperties() {
		return addProperties(super.getProperties(), BCOLOR); 
	}
	
	public class SimplePropertiesComp extends BasicPropertiesComp {
		
		private Canvas bColorSel;
		private Button bColor_Transparent;
		
		public SimplePropertiesComp(final Composite comp, int style) {
			super(comp, style);
			
			bColorSel = addColorSel("Background Color:", (Color)BCOLOR.value);
			bColor_Transparent = addColorTransSel(bColorSel);
			
			/*PBButton a = new PBButton(this, SWT.NONE);
			a.setText("My");*/
		}
		
		public Properties getCurProps() {
			return new SimpleProperties(
					addProperties(super.getCurProps().properties, 
						BCOLOR.newWithValue(bColor_Transparent.getSelection()?
							null:bColorSel.getBackground())));
		}
	}
}
