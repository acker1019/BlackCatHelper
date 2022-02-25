import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class Tab extends JPanel {
	protected innerDB db;
	
	public Tab(innerDB db) {
		this.setName("");
		this.db = db;
		this.setBackground(Color.white);
	}
	
	public Tab(String name, innerDB db) {
		this.setName(name);
		this.db = db;
		this.setBackground(Color.white);
	}
}
