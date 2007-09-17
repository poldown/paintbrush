package my.paintbrush.PointsManager;

public class IDGenerator {
	ID id;
	
	public IDGenerator() {
		id = new ID(0);
	}
	
	public ID generate() {
		return id.next();
	}
	
	public class ID {
		int id;
		
		public ID(int id) {
			this.id = id;
		}
		
		public ID next() {
			return new ID(++this.id);
		}
		
		@Override
		public String toString() {
			return "<" + id + ">";
		}
	}
}
