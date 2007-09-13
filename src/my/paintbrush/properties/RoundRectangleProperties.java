package my.paintbrush.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

public class RoundRectangleProperties extends SimpleProperties {
	public static final String ARCH = "arcH";
	public static final String ARCW = "arcW";
	
	public RoundRectangleProperties(Property... properties) {
		super(properties);
	}
	
	public RoundRectangleProperties() {}
	
	@Override
	public Property[] getProperties() {
		return addProperties(super.getProperties(),
							 new Property(ARCH),
							 new Property(ARCW));
	}
	
	public class RoundRectanglePropertiesComp extends SimplePropertiesComp {
		private Spinner ArcWidthSel;
		private Spinner ArcHeightSel;
		
		public RoundRectanglePropertiesComp(final Composite comp, int style) {
			super(comp, style);
			
			BasicProperties defaultProp = new BasicProperties(
					new Property(ARCW, 30),
					new Property(ARCH, 30));
			
			new Label(this, SWT.NONE).setText("Arc width:");
			
			ArcWidthSel = new Spinner(this, SWT.NONE);
			ArcWidthSel.setMinimum(1);
			ArcWidthSel.setMaximum(360);
			ArcWidthSel.setSelection((Integer)defaultProp.getProperty(ARCW));
			GridData gridData = new GridData();
			gridData.horizontalSpan = 2;
			ArcWidthSel.setLayoutData(gridData);
			
			new Label(this, SWT.NONE).setText("Arc height:");
			
			ArcHeightSel = new Spinner(this, SWT.NONE);
			ArcHeightSel.setMinimum(1);
			ArcHeightSel.setMaximum(360);
			ArcHeightSel.setSelection((Integer)defaultProp.getProperty(ARCH));
			gridData = new GridData();
			gridData.horizontalSpan = 2;
			ArcHeightSel.setLayoutData(gridData);
		}
		
		@Override
		public Properties getCurProps() {
			return new RoundRectangleProperties(
					addProperties(super.getCurProps().properties,
							new Property(ARCW, ArcWidthSel.getSelection()),
							new Property(ARCH, ArcHeightSel.getSelection())));
		}
	}
}