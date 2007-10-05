package my.paintbrush.PbControls;

import my.paintbrush.PbSWT;
import my.paintbrush.Controls.DrawingCanvas;
import my.paintbrush.Events.PbControlEvent;
import my.paintbrush.Properties.Properties;
import my.paintbrush.Tools.DrawingObject;
import my.paintbrush.Tools.DrawingTool;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class PbToolSelector extends Composite {
	
	private Composite parent;
	
	//private Combo toolSel;
	
	private DrawingTool selectedTool = DrawingTool.NONE;
	private Button selectedButton = null;
	
	private static final int MaxButtonsPerLine = 5;
	private static final int ButtonSize = 40;

	private static final String toolKey = "tool";
	
	public PbToolSelector(Composite comp, int style, Composite parent) {
		super(comp, style);
		
		this.parent = parent;
		GridLayout gridLayout = new GridLayout(MaxButtonsPerLine, true);
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		this.setLayout(gridLayout);
		
		//toolSel = addToolSel(style);
		for (DrawingTool tool : DrawingTool.values()) {
			System.out.println("Creating button for tool: " + tool.disName + "...");
			Button toolButton = new Button(this, SWT.TOGGLE);
			toolButton.setData(toolKey, tool);
			toolButton.setToolTipText(tool.disName);
			setSampleImageToButton(toolButton);
			GridData gridData = new GridData(ButtonSize + 6, ButtonSize + 6);
			toolButton.setLayoutData(gridData);
			toolButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					if (selectedButton != null) {
						selectedButton.setSelection(false);
						selectedButton.setEnabled(true);
					}
					Button button = (Button) e.getSource();
					selectedButton = button;
					selectedButton.setEnabled(false);
					DrawingTool tool = (DrawingTool) button.getData(toolKey);
					selectTool(tool);
				}
			});
		}
	}
	
	/*private Combo addToolSel(int style) {
		Label toolSelLabel = new Label(this, SWT.NONE);
		toolSelLabel.setText("Selected Tool:");
		toolSelLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		
		final Combo toolSel = new Combo(this, style);
		for (DrawingTool tool : DrawingTool.values())
			toolSel.add(tool.disName);
		
		toolSel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				selectTool(DrawingTool.values()
						[toolSel.getSelectionIndex()]);
			}
		});
		return toolSel;
	}*/
	
	/*private void setTool(DrawingTool tool) {
		toolSel.setText(tool.disName);
		selectTool(tool);
	}*/
	
	private void setSampleImageToButton(Button toolButton) {
		DrawingTool tool = (DrawingTool) (toolButton.getData(toolKey));
		Image image = new Image(Display.getCurrent(), ButtonSize, ButtonSize);
		image.getImageData().transparentPixel = 0;
		PbDrawable drawable = new PbDrawable(image, ButtonSize, ButtonSize);
		if (!tool.className.equals(""))
			try {
				Properties prop = Class.forName(tool.propertiesClassName).asSubclass(Properties.class).newInstance();
				prop.properties = prop.getProperties();
				DrawingObject instance = tool.getCorrespondingDOCons().newInstance(-1, -1, prop);
				instance.drawSample(drawable);
			} catch (Exception e) {
				e.printStackTrace();
				DrawingCanvas.drawNotAvailable(drawable);
			}
		Image grayedImage = new Image(Display.getCurrent(), image, SWT.IMAGE_GRAY);
		toolButton.setImage(grayedImage );
		
	}

	public void selectTool(DrawingTool tool) {
		selectedTool = tool;
		PbControlEvent event = new PbControlEvent();
		event.tool = tool;
		parent.notifyListeners(PbSWT.ToolSelected, event);
	}

	public DrawingTool getSelectedTool() {
		/*int index = toolSel.getSelectionIndex();
		if (index == -1)
			return DrawingTool.NONE;
		else
			return DrawingTool.values()[index];*/
		return selectedTool;
	}
}
