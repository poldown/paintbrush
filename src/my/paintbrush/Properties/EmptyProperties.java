package my.paintbrush.Properties;

import my.paintbrush.PbSWT;
import my.paintbrush.Events.PbControlEvent;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;

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
		
		public Properties getCurProps() {
			return new EmptyProperties(new Property[] {});
		}
	}
}
