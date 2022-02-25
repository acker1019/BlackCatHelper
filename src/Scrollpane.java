import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;


public class Scrollpane extends JScrollPane {
	public Scrollpane() {
		this.setBorder(BorderFactory.createEmptyBorder());
	}
	
	public Scrollpane(Component in) {
		super(in);
		this.setBorder(BorderFactory.createEmptyBorder());
		this.getVerticalScrollBar().setUnitIncrement(150);
		this.getVerticalScrollBar().setPreferredSize(new Dimension(15, 0));
	}
}
