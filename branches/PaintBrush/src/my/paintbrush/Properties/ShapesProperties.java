package my.paintbrush.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

public class ShapesProperties extends SimpleProperties {
	public static final Property SIDESNUM = new Property("sidesNum",
			3);
	
	public ShapesProperties(Property... properties) {
		super(properties);
	}
	
	public ShapesProperties() {}
	
	@Override
	public Property[] getProperties() {
		return addProperties(super.getProperties(), SIDESNUM);
	}
	
	public class ShapesPropertiesComp extends SimplePropertiesComp {
		private Spinner sidesNumSel;
		
		public ShapesPropertiesComp(final Composite comp, int style) {
			super(comp, style);
			
			sidesNumSel = addSidesNumSel();
		}
		
		private Spinner addSidesNumSel() {
			new Label(this, SWT.NONE).setText("Number of sides:");
			
			Spinner sidesNumSel = new Spinner(this, SWT.NONE);
			sidesNumSel.setMinimum(3);
			sidesNumSel.setMaximum(50);
			sidesNumSel.setSelection((Integer)SIDESNUM.value);
			GridData gridData = new GridData();
			gridData.horizontalSpan = 2;
			sidesNumSel.setLayoutData(gridData);
			sidesNumSel.addSelectionListener(notifyPropChangeSelectionListener);
			return sidesNumSel;
		}
		
		@Override
		public Properties getCurProps() {
			return new ShapesProperties(
					addProperties(super.getCurProps().properties,
							SIDESNUM.newWithValue(sidesNumSel.getSelection())));
		}
	}
}