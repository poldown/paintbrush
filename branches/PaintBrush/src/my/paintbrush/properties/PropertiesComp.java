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
import org.eclipse.swt.widgets.*;

public class PropertiesComp extends Composite {
	
	private Canvas fColorSel;
	private Button fColor_Transparent;
	private Canvas bColorSel;
	private Button bColor_Transparent;
	private Spinner widthSel;
	
	public PropertiesComp(final Composite comp, int style) {
		super(comp, style);
		
		Properties defaultProp = new Properties(3,
				new Color(Display.getCurrent(), 255, 0, 0),
				new Color(Display.getCurrent(), 0, 0, 255),
				30, 30);
		
		this.setLayout(new GridLayout(3, false));
		
		new Label(this, SWT.NONE).setText("Line width:");
		
		widthSel = new Spinner(this, SWT.NONE);
		widthSel.setMinimum(1);
		widthSel.setMaximum(50);
		widthSel.setSelection(defaultProp.width);
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
		fColorSel.setBackground(defaultProp.fColor);
		fColorSel.setLayoutData(new GridData(50, 50));
		fColorSel.addMouseListener(colorSelMouseListener);
		
		fColor_Transparent = new Button(this, SWT.CHECK);
		fColor_Transparent.setText("Transparent");
		fColor_Transparent.setSelection(false);
		fColor_Transparent.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				fColorSel.setEnabled(!fColor_Transparent.getSelection());
			}
		});
		
		Label bColorSelLabel = new Label(this, SWT.NONE);
		bColorSelLabel.setText("Background Color:");
		bColorSelLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
		
		bColorSel = new Canvas(this, SWT.BORDER);
		bColorSel.setBackground(defaultProp.bColor);
		bColorSel.setLayoutData(new GridData(50, 50));
		bColorSel.addMouseListener(colorSelMouseListener);
		
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
		
		this.pack();
	}
	
	private void enableColorSel(Canvas canvas, boolean enable) {
		GC gc = new GC(canvas);
		if (enable) {}
			//Delete all
		else {}
			//Draw disabled
		gc.dispose();
	}
	
	public Properties getCurProps() {
		return new Properties(widthSel.getSelection(),
							  fColor_Transparent.getSelection()?
									  null:fColorSel.getBackground(),
							  bColor_Transparent.getSelection()?
									  null:bColorSel.getBackground(),
							  30,
							  30);
	}
}
