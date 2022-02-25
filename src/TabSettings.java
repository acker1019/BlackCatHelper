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
		super("設定", db);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//line 1
		line = new Line();
		JPanel up = new JPanel();
		up.setBackground(Color.white);
		fontSize_Lb = new JLabel("字型大小：");
		up.add(fontSize_Lb);
		fontSize_TF = new JTextField(3);
		fontSize_TF.setText(String.valueOf(db.meta.fontSize));
		up.add(fontSize_TF);
		JPanel down = new JPanel();
		down.setBackground(Color.white);
		fontSize_illus_Lb = new JLabel("字體大小範圍：15～40");
		down.add(fontSize_illus_Lb);
		line.add(up);
		line.add(down);
		
		this.add(line);
		//end line 1
		
		//line 2
		line = new Line();
		up = new JPanel();
		up.setBackground(Color.white);
		imgSize_Lb = new JLabel("圖示大小：");
		up.add(imgSize_Lb);
		imgSize_TF = new JTextField(3);
		imgSize_TF.setText(String.valueOf(db.meta.imgSize));
		up.add(imgSize_TF);
		down = new JPanel();
		down.setBackground(Color.white);
		imgSize_illus_Lb = new JLabel("圖示大小範圍：35～60");
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
		reSizeIcon_Lb = new JLabel("禁用「調整圖示大小」");
		up.add(reSizeIcon_Lb);
		down = new JPanel();
		down.setBackground(Color.white);
		reSizeIcon_illus_Lb = new JLabel("禁用後圖示大小固定50，但程式啟動速度較快");
		down.add(reSizeIcon_illus_Lb);
		line.add(up);
		line.add(down);
		
		this.add(line);
		//end line3
		
		//line submit
		line = new Line();
		line.setLayout(new FlowLayout());
		h = new BtnHandler();
		back_to_def = new JButton("預設值");
		back_to_def.setFocusable(false);
		back_to_def.addActionListener(h);
		line.add(back_to_def);
		JPanel empty = new JPanel();
		empty.setPreferredSize(new Dimension(100, 0));
		line.add(empty);
		submit = new JButton("套用");
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
					db.stateMsgr.setText("有數字超出範圍喔 ˋ  ˊ   ||   字體大小範圍(15~45)");
					return;
				} else if(imgSize < 35 || imgSize > 60){
					db.stateMsgr.setText("有數字超出範圍喔 ˋ  ˊ   ||   圖示大小範圍(35~60)");
					return;
				} else {
					db.stateMsgr.setText("更新meta資訊  |  . . .");
					db.meta.fontSize = fontSize;
					db.meta.imgSize = imgSize;
					db.meta.isResizeIcon = imgResize;
					db.uploadMeta();
					db.stateMsgr.setText("更新meta資訊  |  重新運算圖片(0%)  |  . . .");
					db.resizeAllIcons(imgSize);
					db.stateMsgr.setText("更新meta資訊  |  重新運算圖片(0%)  |  設定字體大小  |  . . .");
					eM.setFont(fontSize);
					String baned;
					if(imgResize) {
						baned = "啟用";
					} else {
						baned = "禁用";
					}
					db.stateMsgr.setText("設定..勉強....幫你套用了喔≧Ｏ≦   ||   字體大小：" + fontSize + " ;  圖示大小："+ imgSize + " ;  圖示調整大小：" + baned);
				}
			} else if( event == back_to_def ) {
				fontSize_TF.setText("25");
				imgSize_TF.setText("50");
				reSizeIcon_cb.setSelected(true);
				db.stateMsgr.setText("提醒   ||   預設值在套用後才會生效");
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
