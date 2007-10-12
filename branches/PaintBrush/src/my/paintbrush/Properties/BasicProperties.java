package my.paintbrush.Properties;

import my.paintbrush.Controls.ImageCombo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Spinner;

public class BasicProperties extends EmptyProperties {

	public static final Property WIDTH = new Property(
			"width", 2);
	public static final Property FCOLOR = new Property(
			"fCOLOR", Display.getCurrent().getSystemColor(SWT.COLOR_RED));
	public static final Property FCOLOR_TRANS = new Property(
			"fColor_Trans", 255);
	public static final Property LINEDASH = new Property(
			"lineDash", null);
	
	private int[][] dashes = new int[][] {
			null,
			new int[] {1},
			new int[] {5},
			new int[] {2, 3},
			new int[] {5, 1},
			new int[] {1, 1, 2, 1},
			new int[] {2, 2, 4, 2},
			new int[] {4, 2, 2, 2, 2, 2}
	};
	
	public BasicProperties(Property... properties) {
		super(properties);
	}
	
	public BasicProperties() {}
	
	public Property[] getProperties() {
		return addProperties(super.getProperties(), WIDTH, FCOLOR, FCOLOR_TRANS, LINEDASH); 
	}
	
	public class BasicPropertiesComp extends EmptyPropertiesComp {
		
		protected Canvas fColorSel;
		protected Scale fColor_Transparent;
		protected Spinner widthSel;
		protected ImageCombo lineDashSel;
		
		public BasicPropertiesComp(final Composite comp, int style) {
			super(comp, style);
			
			this.setLayout(new GridLayout(3, false));
			
			widthSel = addWidthSel();
			lineDashSel = addLineDashSel();
			fColorSel = addColorSel("Foreground Color:", (Color)FCOLOR.value);
			fColor_Transparent = addColorTransSel(fColorSel, (Integer)FCOLOR_TRANS.value);
		}
		
		private Spinner addWidthSel() {
			new Label(this, SWT.NONE).setText("Line width:");
			
			Spinner widthSel = new Spinner(this, SWT.NONE);
			widthSel.setMinimum(1);
			widthSel.setMaximum(50);
			widthSel.setSelection((Integer)WIDTH.value);
			GridData gridData = new GridData();
			gridData.horizontalSpan = 2;
			widthSel.setLayoutData(gridData);
			widthSel.addSelectionListener(notifyPropChangeSelectionListener);
			return widthSel;
		}
		
		private ImageCombo addLineDashSel() {
			Label lineDashSelLabel = new Label(this, SWT.NONE);
			lineDashSelLabel.setText("Line Style:");
			lineDashSelLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
			
			ImageCombo lineDashSel = new ImageCombo(this, SWT.FLAT);
			for (int[] dash : dashes)
				lineDashSel.add(getLineDashImage(dash));
			lineDashSel.select(getLineDashIndex((int[])LINEDASH.value));
			GridData gridData = new GridData();
			gridData.horizontalSpan = 2;
			lineDashSel.setLayoutData(gridData);
			lineDashSel.addSelectionListener(notifyPropChangeSelectionListener);
			return lineDashSel;
		}
		
		private int getLineDashIndex(int[] lineDash) {
			for (int i = 0; i < dashes.length; i++) {
				if (dashes[i] == null) {
					if (lineDash == null) return i;
				} else {
					if (lineDash.length != dashes[i].length) continue;
					boolean lineDashEquals = true;
					for (int j = 0; j < dashes[i].length; j++) {
						if (dashes[i][j] != lineDash[j])
							lineDashEquals = false;
					}
					if (lineDashEquals) return i;
				}
			}
			return -1;
		}
		
		private Image getLineDashImage(int[] dashes) {
			Image image = new Image(Display.getCurrent(), 100, 15);
			GC gc = new GC(image);
			gc.setLineWidth(widthSel.getSelection());
			gc.setLineDash(dashes);
			gc.drawLine(0, 8, 100, 8);
			gc.dispose();
			return image;
		}
		
		public Properties getCurProps() {
			return new BasicProperties(
					addProperties(super.getCurProps().properties, 
						WIDTH.newWithValue(widthSel.getSelection()),
						FCOLOR.newWithValue(fColorSel.getBackground()),
						FCOLOR_TRANS.newWithValue(fColor_Transparent.getSelection()),
						LINEDASH.newWithValue(dashes[lineDashSel.getSelectionIndex()])));
		}
	}
}
