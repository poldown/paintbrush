package my.paintbrush.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Scale;

public class SimpleProperties extends BasicProperties {

	public static final Property BCOLOR = new Property(
			"bCOLOR", Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
	public static final Property BCOLOR_TRANS = new Property(
			"bColor_Trans", 255);
	
	public SimpleProperties(Property... properties) {
		super(properties);
	}
	
	public SimpleProperties() {}
	
	public Property[] getProperties() {
		return addProperties(super.getProperties(), BCOLOR, BCOLOR_TRANS); 
	}
	
	public class SimplePropertiesComp extends BasicPropertiesComp {
		
		protected Button switchBF;
		protected Canvas bColorSel;
		protected Scale bColor_Transparent;
		
		public SimplePropertiesComp(final Composite comp, int style) {
			super(comp, style);
			
			switchBF = addSwitchBFButton();
			bColorSel = addColorSel("Background Color:", (Color)BCOLOR.value);
			bColor_Transparent = addColorTransSel(bColorSel, (Integer)BCOLOR_TRANS.value);
			
			/*PBButton a = new PBButton(this, SWT.NONE);
			a.setText("My");*/
		}
		
		private Button addSwitchBFButton() {
			Button switchBF = new Button(this, SWT.PUSH);
			switchBF.setText("Switch Foreground and Background colors");
			GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, true, 3, 1);
			gridData.heightHint = 18;
			switchBF.setLayoutData(gridData);
			switchBF.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					//Switch colors
					Color fColorSelTemp = fColorSel.getBackground();
					fColorSel.setBackground(bColorSel.getBackground());
					bColorSel.setBackground(fColorSelTemp);
					//Switch transparency
					int fColor_TransparentTemp = fColor_Transparent.getSelection();
					fColor_Transparent.setSelection(bColor_Transparent.getSelection());
					bColor_Transparent.setSelection(fColor_TransparentTemp);
					//Update transparency state
					fColor_Transparent.notifyListeners(SWT.Selection, new Event());
					bColor_Transparent.notifyListeners(SWT.Selection, new Event());
				}
			});
			return switchBF;
		}

		public Properties getCurProps() {
			return new SimpleProperties(
					addProperties(super.getCurProps().properties, 
						BCOLOR.newWithValue(bColorSel.getBackground()),
						BCOLOR_TRANS.newWithValue(bColor_Transparent.getSelection())));
		}
	}
}
