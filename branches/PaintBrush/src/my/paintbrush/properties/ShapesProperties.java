package my.paintbrush.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

public class ShapesProperties extends SimpleProperties {
	public static final String SIDESNUM = "sidesNum";
	
	public ShapesProperties(Property... properties) {
		super(properties);
	}
	
	public ShapesProperties() {}
	
	@Override
	public Property[] getProperties() {
		return addProperties(super.getProperties(),
							 new Property(SIDESNUM));
	}
	
	public class ShapesPropertiesComp extends SimplePropertiesComp {
		private Spinner sidesNumSel;
		
		public ShapesPropertiesComp(final Composite comp, int style) {
			super(comp, style);
			
			BasicProperties defaultProp = new BasicProperties(
					new Property(SIDESNUM, 3));
			
			new Label(this, SWT.NONE).setText("Number of sides:");
			
			sidesNumSel = new Spinner(this, SWT.NONE);
			sidesNumSel.setMinimum(3);
			sidesNumSel.setMaximum(50);
			sidesNumSel.setSelection((Integer)defaultProp.getProperty(SIDESNUM));
			GridData gridData = new GridData();
			gridData.horizontalSpan = 2;
			sidesNumSel.setLayoutData(gridData);
		}
		
		@Override
		public Properties getCurProps() {
			return new ShapesProperties(
					addProperties(super.getCurProps().properties,
							new Property(SIDESNUM, sidesNumSel.getSelection())));
		}
	}
}