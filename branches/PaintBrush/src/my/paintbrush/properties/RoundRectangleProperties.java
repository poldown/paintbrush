package my.paintbrush.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

public class RoundRectangleProperties extends SimpleProperties {
	public static final Property ARCH = new Property("arcH",
			30);
	public static final Property ARCW = new Property("arcW",
			30);
	
	public RoundRectangleProperties(Property... properties) {
		super(properties);
	}
	
	public RoundRectangleProperties() {}
	
	@Override
	public Property[] getProperties() {
		return addProperties(super.getProperties(), ARCH, ARCW);
	}
	
	public class RoundRectanglePropertiesComp extends SimplePropertiesComp {
		private Spinner ArcWidthSel;
		private Spinner ArcHeightSel;
		
		public RoundRectanglePropertiesComp(final Composite comp, int style) {
			super(comp, style);
			
			new Label(this, SWT.NONE).setText("Arc width:");
			
			ArcWidthSel = new Spinner(this, SWT.NONE);
			ArcWidthSel.setMinimum(1);
			ArcWidthSel.setMaximum(360);
			ArcWidthSel.setSelection((Integer)ARCW.value);
			GridData gridData = new GridData();
			gridData.horizontalSpan = 2;
			ArcWidthSel.setLayoutData(gridData);
			
			new Label(this, SWT.NONE).setText("Arc height:");
			
			ArcHeightSel = new Spinner(this, SWT.NONE);
			ArcHeightSel.setMinimum(1);
			ArcHeightSel.setMaximum(360);
			ArcHeightSel.setSelection((Integer)ARCH.value);
			gridData = new GridData();
			gridData.horizontalSpan = 2;
			ArcHeightSel.setLayoutData(gridData);
		}
		
		@Override
		public Properties getCurProps() {
			return new RoundRectangleProperties(
					addProperties(super.getCurProps().properties,
							ARCW.newWithValue(ArcWidthSel.getSelection()),
							ARCH.newWithValue(ArcHeightSel.getSelection())));
		}
	}
}