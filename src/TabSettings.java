import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class TabSettings extends Tab {
	private EventManager eM;
	private BtnHandler h;
	
	private Line line;
	//line 1
	private JLabel fontSize_Lb;
	private JTextField fontSize_TF;
	private JLabel fontSize_illus_Lb;
	//line 2
	private JLabel imgSize_Lb;
	private JTextField imgSize_TF;
	private JLabel imgSize_illus_Lb;
	//line 3
	private JLabel reSizeIcon_Lb;
	private JCheckBox reSizeIcon_cb;
	private JLabel reSizeIcon_illus_Lb;
	//submit
	private JButton back_to_def;
	private JButton submit;
	
	public TabSettings(innerDB db) {
		super("�]�w", db);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//line 1
		line = new Line();
		JPanel up = new JPanel();
		up.setBackground(Color.white);
		fontSize_Lb = new JLabel("�r���j�p�G");
		up.add(fontSize_Lb);
		fontSize_TF = new JTextField(3);
		fontSize_TF.setText(String.valueOf(db.meta.fontSize));
		up.add(fontSize_TF);
		JPanel down = new JPanel();
		down.setBackground(Color.white);
		fontSize_illus_Lb = new JLabel("�r��j�p�d��G15��40");
		down.add(fontSize_illus_Lb);
		line.add(up);
		line.add(down);
		
		this.add(line);
		//end line 1
		
		//line 2
		line = new Line();
		up = new JPanel();
		up.setBackground(Color.white);
		imgSize_Lb = new JLabel("�ϥܤj�p�G");
		up.add(imgSize_Lb);
		imgSize_TF = new JTextField(3);
		imgSize_TF.setText(String.valueOf(db.meta.imgSize));
		up.add(imgSize_TF);
		down = new JPanel();
		down.setBackground(Color.white);
		imgSize_illus_Lb = new JLabel("�ϥܤj�p�d��G35��60");
		down.add(imgSize_illus_Lb);
		line.add(up);
		line.add(down);
		
		this.add(line);
		//end line 2
		
		//line3
		line = new Line();
		up = new JPanel();
		up.setBackground(Color.white);
		reSizeIcon_cb = new JCheckBox();
		ChangeHandler ch = new ChangeHandler();
		reSizeIcon_cb.addChangeListener(ch);
		reSizeIcon_cb.setSelected(!db.meta.isResizeIcon);
		up.add(reSizeIcon_cb);
		reSizeIcon_Lb = new JLabel("�T�Ρu�վ�ϥܤj�p�v");
		up.add(reSizeIcon_Lb);
		down = new JPanel();
		down.setBackground(Color.white);
		reSizeIcon_illus_Lb = new JLabel("�T�Ϋ�ϥܤj�p�T�w50�A���{���Ұʳt�׸���");
		down.add(reSizeIcon_illus_Lb);
		line.add(up);
		line.add(down);
		
		this.add(line);
		//end line3
		
		//line submit
		line = new Line();
		line.setLayout(new FlowLayout());
		h = new BtnHandler();
		back_to_def = new JButton("�w�]��");
		back_to_def.setFocusable(false);
		back_to_def.addActionListener(h);
		line.add(back_to_def);
		JPanel empty = new JPanel();
		empty.setPreferredSize(new Dimension(100, 0));
		line.add(empty);
		submit = new JButton("�M��");
		submit.setFocusable(false);
		submit.addActionListener(h);
		line.add(submit);
		
		this.add(line);
		//end line submit
	}
	
	private class Line extends JPanel {
		public Line() {
			this.setBackground(Color.white);
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			Border empty = BorderFactory.createEmptyBorder(5, 5, 5, 5);
			Border blackline = BorderFactory.createLineBorder(Color.gray);
			this.setBorder(BorderFactory.createCompoundBorder(blackline, empty));
		}
	}
	
	private class BtnHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object event = e.getSource();
			if( event == submit ) {
				int fontSize = Integer.parseInt(fontSize_TF.getText());
				int imgSize = Integer.parseInt(imgSize_TF.getText());
				Boolean imgResize = !reSizeIcon_cb.isSelected();
				if(fontSize < 15 || fontSize > 40) {
					db.stateMsgr.setText("���Ʀr�W�X�d��� ��  ��   ||   �r��j�p�d��(15~45)");
					return;
				} else if(imgSize < 35 || imgSize > 60){
					db.stateMsgr.setText("���Ʀr�W�X�d��� ��  ��   ||   �ϥܤj�p�d��(35~60)");
					return;
				} else {
					db.stateMsgr.setText("��smeta��T  |  . . .");
					db.meta.fontSize = fontSize;
					db.meta.imgSize = imgSize;
					db.meta.isResizeIcon = imgResize;
					db.uploadMeta();
					db.stateMsgr.setText("��smeta��T  |  ���s�B��Ϥ�(0%)  |  . . .");
					db.resizeAllIcons(imgSize);
					db.stateMsgr.setText("��smeta��T  |  ���s�B��Ϥ�(0%)  |  �]�w�r��j�p  |  . . .");
					eM.setFont(fontSize);
					String baned;
					if(imgResize) {
						baned = "�ҥ�";
					} else {
						baned = "�T��";
					}
					db.stateMsgr.setText("�]�w..�j�j....���A�M�ΤF��٢ݡ�   ||   �r��j�p�G" + fontSize + " ;  �ϥܤj�p�G"+ imgSize + " ;  �ϥܽվ�j�p�G" + baned);
				}
			} else if( event == back_to_def ) {
				fontSize_TF.setText("25");
				imgSize_TF.setText("50");
				reSizeIcon_cb.setSelected(true);
				db.stateMsgr.setText("����   ||   �w�]�Ȧb�M�Ϋ�~�|�ͮ�");
			}
		}
	}
	
	private class ChangeHandler implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent arg0) {
			if(reSizeIcon_cb.isSelected()) {
				imgSize_TF.setText("50");
				imgSize_TF.setEditable(false);
			} else {
				imgSize_TF.setText("" + db.meta.imgSize);
				imgSize_TF.setEditable(true);
			}
		}
	}
	
	public void addEventManager(EventManager e) {
		eM = e;
	}
}
