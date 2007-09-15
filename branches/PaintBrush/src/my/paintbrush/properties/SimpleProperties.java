package my.paintbrush.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

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
			
			Label bColorSelLabel = new Label(this, SWT.NONE);
			bColorSelLabel.setText("Background Color:");
			bColorSelLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
			
			bColorSel = new Canvas(this, SWT.BORDER);
			bColorSel.setBackground((Color)BCOLOR.value);
			bColorSel.setLayoutData(new GridData(50, 50));
			bColorSel.addMouseListener(getColSelMouseListener(comp.getShell()));
			
			bColor_Transparent = new Button(this, SWT.CHECK);
			bColor_Transparent.setText("Transparent");
			bColor_Transparent.setSelection(false);
			bColor_Transparent.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					bColorSel.setEnabled(!bColor_Transparent.getSelection());
					enableColorSel(bColorSel, !bColor_Transparent.getSelection());
				}
			});
			
			/*PBButton a = new PBButton(this, SWT.NONE);
			a.setText("My");*/
		}
		
		private MouseListener getColSelMouseListener(final Shell shell) {
			return new MouseAdapter() {
				public void mouseDoubleClick(MouseEvent e) {
					ColorDialog dialog = new ColorDialog(shell);
					dialog.setRGB(((Canvas)e.getSource()).getBackground().getRGB());
					RGB selRGB = dialog.open();
					if (selRGB != null)
						((Canvas)e.getSource()).setBackground(new Color(Display.getCurrent(), selRGB));
				}
			};
		}
		
		public Properties getCurProps() {
			return new SimpleProperties(
					addProperties(super.getCurProps().properties, 
						BCOLOR.newWithValue(bColor_Transparent.getSelection()?
							null:bColorSel.getBackground())));
		}
	}
}
