package my.paintbrush.Properties;

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
		private Spinner arcWidthSel;
		private Spinner arcHeightSel;
		
		public RoundRectanglePropertiesComp(final Composite comp, int style) {
			super(comp, style);
			
			arcWidthSel = addArcWidthSel();
			arcHeightSel = addArcHeightSel();
		}
		
		private Spinner addArcWidthSel() {
			new Label(this, SWT.NONE).setText("Arc width:");
			
			Spinner arcWidthSel = new Spinner(this, SWT.NONE);
			arcWidthSel.setMinimum(1);
			arcWidthSel.setMaximum(360);
			arcWidthSel.setSelection((Integer)ARCW.value);
			GridData gridData = new GridData();
			gridData.horizontalSpan = 2;
			arcWidthSel.setLayoutData(gridData);
			return arcWidthSel;
		}
		
		private Spinner addArcHeightSel() {
			new Label(this, SWT.NONE).setText("Arc height:");
			
			Spinner arcHeightSel = new Spinner(this, SWT.NONE);
			arcHeightSel.setMinimum(1);
			arcHeightSel.setMaximum(360);
			arcHeightSel.setSelection((Integer)ARCH.value);
			GridData gridData = new GridData();
			gridData.horizontalSpan = 2;
			arcHeightSel.setLayoutData(gridData);
			return arcHeightSel;
		}
		
		@Override
		public Properties getCurProps() {
			return new RoundRectangleProperties(
					addProperties(super.getCurProps().properties,
							ARCW.newWithValue(arcWidthSel.getSelection()),
							ARCH.newWithValue(arcHeightSel.getSelection())));
		}
	}
}