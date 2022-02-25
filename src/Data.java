import javax.swing.ImageIcon;

public class Data {
		public String id;
		public ImageIcon img;
		public ImageIcon img_lowlight;
		public String race;
		public String attribute;
		public int level;
		
		public Data(String id, String race, String attribute, int level, ImageIcon img, ImageIcon img_lowlight) {
			this.id = id;
			this.race = race;
			this.attribute = attribute;
			this.level = level;
			this.img = img;
			this.img_lowlight = img_lowlight;
		}
		
		public String get(int select) {
			switch(select) {
				case 0:
					return attribute;
				case 1:
					return race;
				case 2:
					return String.valueOf(level);
				case 3:
					return id;
				default:
					return null;
			}
		}
	}