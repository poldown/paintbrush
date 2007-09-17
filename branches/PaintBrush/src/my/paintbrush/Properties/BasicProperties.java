package my.paintbrush.Properties;

import my.paintbrush.Controls.ImageCombo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

public class BasicProperties extends EmptyProperties {

	public static final Property WIDTH = new Property(
			"width", 3);
	public static final Property FCOLOR = new Property(
			"fColor", Display.getCurrent().getSystemColor(SWT.COLOR_RED));
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
		return addProperties(super.getProperties(), WIDTH, FCOLOR, LINEDASH); 
	}
	
	public class BasicPropertiesComp extends EmptyPropertiesComp {
		
		private Canvas fColorSel;
		private Button fColor_Transparent;
		private Spinner widthSel;
		private ImageCombo lineDashSel;
		
		public BasicPropertiesComp(final Composite comp, int style) {
			super(comp, style);
			
			this.setLayout(new GridLayout(3, false));
			
			widthSel = addWidthSel();
			lineDashSel = addLineDashSel();
			fColorSel = addColorSel("Foreground Color:", (Color)FCOLOR.value);
			fColor_Transparent = addColorTransSel(fColorSel);
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
			return lineDashSel;
		}
		
		protected Canvas addColorSel(String labelText, Color initialColor) {
			Label colorSelLabel = new Label(this, SWT.NONE);
			colorSelLabel.setText(labelText);
			colorSelLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
			
			Canvas colorSel = new Canvas(this, SWT.BORDER);
			colorSel.setBackground(initialColor);
			colorSel.setLayoutData(new GridData(50, 50));
			colorSel.addMouseListener(getColSelMouseListener(this.getShell()));
			return colorSel;
		}
		
		protected Button addColorTransSel(final Canvas colorSel) {
			//TODO: BUG! Transparancy isn't related to any property
			//		causing a new created tool to lose its previous
			//		state.
			final Button color_Transparency = new Button(this, SWT.CHECK);
			color_Transparency.setText("Transparent");
			color_Transparency.setSelection(false);
			color_Transparency.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					colorSel.setEnabled(!color_Transparency.getSelection());
					enableColorSel(colorSel, !color_Transparency.getSelection());
				}
			});
			return color_Transparency;
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
		
		protected void enableColorSel(final Canvas canvas, boolean enable) {
			final String enabledKey = "enabled";
			final String enabled = "true";
			final String disabled = "false";
			
			if (canvas.getData(enabledKey) == null)
				canvas.addPaintListener(new PaintListener() {
					public void paintControl(PaintEvent e) {
						GC gc = new GC(canvas);
						if (canvas.getData(enabledKey).equals(enabled)) {
							gc.setForeground(canvas.getBackground());
							gc.fillRectangle(0, 0, canvas.getSize().x, canvas.getSize().y);
						} else if (canvas.getData(enabledKey).equals(disabled)) {
							//gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
							//gc.fillRectangle(0, 0, canvas.getSize().x, canvas.getSize().y);
							gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
							gc.drawLine(0, 0, 50, 50);
							gc.drawLine(50, 0, 0, 50);
						}
						gc.dispose();
					}
				});
			canvas.setData(enabledKey, enable?enabled:disabled);
			canvas.notifyListeners(SWT.Paint, new Event());
		}
		
		public Properties getCurProps() {
			return new BasicProperties(
					addProperties(super.getCurProps().properties, 
						WIDTH.newWithValue(widthSel.getSelection()),
						FCOLOR.newWithValue(fColor_Transparent.getSelection()?
							null:fColorSel.getBackground()),
						LINEDASH.newWithValue(dashes[lineDashSel.getSelectionIndex()])));
		}
	}
}
