package my.paintbrush.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

public class BasicProperties extends Properties {

	public static final String WIDTH = "width";
	public static final String FCOLOR = "fColor";
	
	public BasicProperties(Property... properties) {
		super(properties);
	}
	
	public BasicProperties() {}
	
	public Property[] getProperties() {
		return new Property[] {	
				new Property(WIDTH),
				new Property(FCOLOR)}; 
	}
	
	public class BasicPropertiesComp extends PropertiesComp {
		
		private Canvas fColorSel;
		private Button fColor_Transparent;
		private Spinner widthSel;
		
		public BasicPropertiesComp(final Composite comp, int style) {
			super(comp, style);
			
			BasicProperties defaultProp = new BasicProperties(
					new Property(WIDTH, 3),
					new Property(FCOLOR, 
						Display.getCurrent().getSystemColor(SWT.COLOR_RED)));
			
			this.setLayout(new GridLayout(3, false));
			
			new Label(this, SWT.NONE).setText("Line width:");
			
			widthSel = new Spinner(this, SWT.NONE);
			widthSel.setMinimum(1);
			widthSel.setMaximum(50);
			widthSel.setSelection((Integer)defaultProp.getProperty(WIDTH));
			GridData gridData = new GridData();
			gridData.horizontalSpan = 2;
			widthSel.setLayoutData(gridData);
			
			MouseListener colorSelMouseListener = new MouseAdapter() {
				public void mouseDoubleClick(MouseEvent e) {
					ColorDialog dialog = new ColorDialog(comp.getShell());
					dialog.setRGB(((Canvas)e.getSource()).getBackground().getRGB());
					RGB selRGB = dialog.open();
					if (selRGB != null)
						((Canvas)e.getSource()).setBackground(new Color(Display.getCurrent(), selRGB));
				}
			};
			
			Label fColorSelLabel = new Label(this, SWT.NONE);
			fColorSelLabel.setText("Foreground Color:");
			fColorSelLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
			
			fColorSel = new Canvas(this, SWT.BORDER);
			fColorSel.setBackground((Color)defaultProp.getProperty(FCOLOR));
			fColorSel.setLayoutData(new GridData(50, 50));
			fColorSel.addMouseListener(colorSelMouseListener);
			
			fColor_Transparent = new Button(this, SWT.CHECK);
			fColor_Transparent.setText("Transparent");
			fColor_Transparent.setSelection(false);
			fColor_Transparent.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					fColorSel.setEnabled(!fColor_Transparent.getSelection());
					enableColorSel(fColorSel, !fColor_Transparent.getSelection());
				}
			});
		}
		
		protected void enableColorSel(Canvas canvas, boolean enable) {
			GC gc = new GC(canvas);
			if (enable) {
				gc.setForeground(canvas.getBackground());
				gc.fillRectangle(0, 0, canvas.getSize().x, canvas.getSize().y);
			} else {
				//gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
				//gc.fillRectangle(0, 0, canvas.getSize().x, canvas.getSize().y);
				gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
				gc.drawLine(0, 0, 50, 50);
				gc.drawLine(50, 0, 0, 50);
			}
				//Draw disabled
			gc.dispose();
		}
		
		public Properties getCurProps() {
			return new BasicProperties(
					new Property(WIDTH, widthSel.getSelection()),
					new Property(FCOLOR, fColor_Transparent.getSelection()?
						null:fColorSel.getBackground()));
		}
	}
}
