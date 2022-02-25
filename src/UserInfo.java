import java.util.ArrayList;

import javax.swing.ImageIcon;

public class UserInfo {
		public String name;
		public String fileName;
		public ArrayList<NumGroup> data;
		
		public UserInfo(String name, String fileName) {
			this.name = name;
			this.fileName = fileName;
			data = new ArrayList<NumGroup>();
		}
		
		public NumGroup getNumGroup(String id) {
			for( NumGroup item : data ) {
				if( id.equals(item.cardID) ) {
					return item;
				}
			}
			NumGroup tmp = new NumGroup(null, 0, 0, false);
			data.add(tmp);
			return tmp;
		}
	}