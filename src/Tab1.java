import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class Tab1 extends Tab {
	public RecordsPanel recordsP;
	
	public Tab1(innerDB db) {
		super("¯À§÷", db);
		this.setLayout(new BorderLayout());
		
		recordsP = new RecordsPanel(db);
		this.add(recordsP, BorderLayout.CENTER);
	}
}
