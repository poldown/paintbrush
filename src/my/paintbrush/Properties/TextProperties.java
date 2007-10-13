package my.paintbrush.Properties;

import my.paintbrush.PbSWT;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Text;

public class TextProperties extends EmptyProperties {
	public static final Property FCOLOR = new Property(
			"fCOLOR", Display.getCurrent().getSystemColor(PbSWT.COLOR_RED));
	public static final Property FCOLOR_TRANS = new Property(
			"fColor_Trans", 255);
	public static final Property TEXT = new Property("Text",
			"<Enter text>");
	public static final Property FONT = new Property("Font",
			new Font(Display.getCurrent(), "Arial", 12, PbSWT.NONE));
	
	public TextProperties(Property... properties) {
		super(properties);
	}
	
	public TextProperties() {}
	
	@Override
	public Property[] getProperties() {
		return addProperties(super.getProperties(), FCOLOR, FCOLOR_TRANS, TEXT, FONT);
	}
	
	public class TextPropertiesComp extends EmptyPropertiesComp {
		
		protected Canvas fColorSel;
		protected Scale fColor_Transparent;
		protected Button fontSel;
		protected Text textSel;
		
		public TextPropertiesComp(final Composite comp, int style) {
			super(comp, style);
			
			this.setLayout(new GridLayout(3, false));
			
			fColorSel = addColorSel("Foreground Color:", (Color)FCOLOR.value);
			fColor_Transparent = addColorTransSel(fColorSel, (Integer)FCOLOR_TRANS.value);
			textSel = addTextSel((String)TEXT.value);
			fontSel = addFontSel((Font)FONT.value, textSel);
		}
		
		private Text addTextSel(String value) {
			Label label = new Label(this, PbSWT.NONE);
			label.setText("Text:");
			label.setLayoutData(new GridData(PbSWT.FILL, PbSWT.TOP, false, false));
			
			Text textSel = new Text(this, PbSWT.MULTI | PbSWT.V_SCROLL | PbSWT.H_SCROLL);
			textSel.setText(value);
			GridData gridData = new GridData();
			gridData.heightHint = 54;
			gridData.horizontalSpan = 2;
			gridData.horizontalAlignment = PbSWT.FILL;
			textSel.setLayoutData(gridData);
			textSel.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					notifyPropChange();
				}
			});
			return textSel;
		}

		private Button addFontSel(Font value, final Text textSel) {
			Label label = new Label(this, PbSWT.NONE);
			label.setText("Font:");
			label.setLayoutData(new GridData(PbSWT.FILL, PbSWT.TOP, false, false));
			
			Button fontSel = new Button(this, PbSWT.PUSH);
			fontSel.setText(getFontText(value.getFontData()[0]));
			fontSel.setData(value);
			fontSel.setLayoutData(new GridData(PbSWT.FILL, PbSWT.TOP, false, false, 2, 1));
			fontSel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					Button button = (Button) e.getSource();
					FontDialog dialog = new FontDialog(button.getShell());
					dialog.setFontList(((Font) button.getData()).getFontData());
					FontData fontData = dialog.open();
					if (fontData != null) {
						fontData.setHeight(12);
						Font selFont = new Font(Display.getCurrent(), fontData);
						button.setData(selFont);
						button.setText(getFontText(fontData));
						textSel.setFont(selFont);
						notifyPropChange();
					}
				}
			});
			
			textSel.setFont(value);
			
			return fontSel;
		}

		protected String getFontText(FontData fontData) {
			return 	fontData.getName() + 
					(((fontData.getStyle() & PbSWT.BOLD) > 0)?", Bold":"") +
					(((fontData.getStyle() & PbSWT.ITALIC) > 0)?", Italic":"") + 
					((fontData.data.lfUnderline > 0)?", Underline":"") +
					((fontData.data.lfStrikeOut > 0)?", StrikeOut":"");
		}

		@Override
		public Properties getCurProps() {
			return new TextProperties(
					addProperties(super.getCurProps().properties,
							FCOLOR.newWithValue(fColorSel.getBackground()),
							FCOLOR_TRANS.newWithValue(fColor_Transparent.getSelection()),
							TEXT.newWithValue(textSel.getText()),
							FONT.newWithValue(fontSel.getData())));
		}
	}
}
