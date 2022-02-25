import java.awt.Color;

import javax.swing.JTextField;
import javax.swing.border.Border;

public class Digitbox extends JTextField    {
	public Digitbox(int width, Color bg) {
		super(width);
		this.setBackground(bg);
	}
	
	@Override
	public void setBorder(Border border) {
        // No!
    }
}