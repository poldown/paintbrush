package my.paintbrush.properties;

public class Property {

	public String name;
	/*public Class<?> type;*/
	public Object value;
	
	public Property(String name, /*Class<?> type, */Object value) {
		this.name = name;
		//this.type = type;
		this.value = value;
	}
	
	public Property(String name) {
		this.name = name;
		//this.type = null;
		this.value = null;
	}
}
