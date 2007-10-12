package my.paintbrush.Properties;

import my.paintbrush.PbSWT;
import my.paintbrush.Controls.DrawingCanvas;
import my.paintbrush.Events.PbControlEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;

public class EmptyProperties extends Properties {

	public EmptyProperties(Property... properties) {
		super(properties);
	}
	
	public EmptyProperties() {}
	
	public Property[] getProperties() {
		return new Property[] {}; 
	}
	
	public class EmptyPropertiesComp extends PropertiesComp {
		
		protected final SelectionListener notifyPropChangeSelectionListener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				notifyPropChange();
			}
		};
		
		protected void notifyPropChange() {
			this.notifyListeners(PbSWT.PropertiesChanged, new PbControlEvent());
		}
		
		public EmptyPropertiesComp(Composite comp, int style) {
			super(comp, style);
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
		
		protected Scale addColorTransSel(final Canvas colorSel, int initialTrans) {
			final Scale color_Transparency = new Scale(this, SWT.CHECK);
			//color_Transparency.setText("Transparent");
			color_Transparency.setMinimum(0);
			color_Transparency.setMaximum(255);
			color_Transparency.setSelection(initialTrans);
			color_Transparency.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					notifyPropChange();
					colorSel.setEnabled(color_Transparency.getSelection() != 0);
					enableColorSel(colorSel, color_Transparency.getSelection() != 0);
				}
			});
			color_Transparency.notifyListeners(SWT.Selection, new Event());
			return color_Transparency;
		}
		
		private MouseListener getColSelMouseListener(final Shell shell) {
			return new MouseAdapter() {
				public void mouseDoubleClick(MouseEvent e) {
					ColorDialog dialog = new ColorDialog(shell);
					dialog.setRGB(((Canvas)e.getSource()).getBackground().getRGB());
					RGB selRGB = dialog.open();
					if (selRGB != null) {
						((Canvas)e.getSource()).setBackground(new Color(Display.getCurrent(), selRGB));
						notifyPropChange();
					}
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
						if (canvas.getData(enabledKey).equals(enabled)) {
							GC gc = new GC(canvas);
							gc.setForeground(canvas.getBackground());
							gc.fillRectangle(0, 0, canvas.getSize().x, canvas.getSize().y);
							gc.dispose();
						} else if (canvas.getData(enabledKey).equals(disabled)) {
							DrawingCanvas.drawNotAvailable(canvas);
						}
					}
				});
			canvas.setData(enabledKey, enable?enabled:disabled);
			canvas.notifyListeners(SWT.Paint, new Event());
		}
		
		public Properties getCurProps() {
			return new EmptyProperties(new Property[] {});
		}
	}
}
