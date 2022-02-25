import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


	public class RItem extends JPanel {
		public RItem self;
		
		public String id;
		public int max;
		public int cur;
		
		public JLabel img;
		public Digitbox max_TF;
		public Digitbox cur_TF;
		public JButton plus;
		public JButton minus;
		public Digitbox space;
		
		private innerDB db;
		public NumGroup nGroup;
		private UserInfo uInfo;
		
		public RItem(String id, ImageIcon img, innerDB db, Color item_bg, NumGroup nGroup, UserInfo uInfo) {
			self = this;
			this.id = id;
			this.db = db;
			this.nGroup = nGroup;
			this.uInfo = uInfo;
			
			this.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.white));
			this.setBackground(item_bg);
			
			this.img = new JLabel(img);
			this.max = nGroup.tolNeeded;
			this.cur = nGroup.curNum;
			
			BtnHandler h = new BtnHandler();
			KeyboardDetector kd;
			
			this.add(this.img);
			JLabel l = new JLabel();
			l.setText("(");
			this.add(l);
			String s = String.valueOf(cur);
			cur_TF = new Digitbox(s.length(), item_bg);
			cur_TF.setText("" + cur);
			cur_TF.setEditable(false);
			kd = new KeyboardDetector(cur_TF);
			cur_TF.addKeyListener(kd);
			this.add(cur_TF);
			l = new JLabel();
			l.setText("/");
			this.add(l);
			s = String.valueOf(max);
			max_TF = new Digitbox(s.length(), item_bg);
			max_TF.setText("" + max);
			max_TF.setEditable(false);
			kd = new KeyboardDetector(max_TF);
			max_TF.addKeyListener(kd);
			this.add(max_TF);
			l = new JLabel();
			l.setText(")");
			this.add(l);
			space = new Digitbox(2, item_bg);
			space.setEnabled(false);
			this.add(space);
			plus = new JButton("+");
			plus.setFocusable(false);
			plus.addActionListener(h);
			this.add(plus);
			minus = new JButton("-");
			minus.setFocusable(false);
			minus.addActionListener(h);
			minus.setVisible(false);
			this.add(minus);
			
			reLength();
			this.setVisible(nGroup.dispaly);
		}
		
		public void refresh() {
			this.setVisible(false);
			this.setVisible(true);
		}
		
		private void reLength() {
			cur_TF.setColumns(cur_TF.getText().length());
			max_TF.setColumns(max_TF.getText().length());
			space.setColumns(4-cur_TF.getColumns()-max_TF.getColumns());
		}
		
		private class BtnHandler implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				Object event = e.getSource();
				max = Integer.parseInt( max_TF.getText() );
				cur = Integer.parseInt( cur_TF.getText() );
				if( event == plus ) {
					if( cur < max ) {
						cur = Integer.parseInt( cur_TF.getText() ) + 1;
						nGroup.curNum = cur;
						db.uploadUserInfo(uInfo);
					} else {
						db.stateMsgr.setText("不能再大了");
					}
				} else if( event == minus ) {
					if( cur > 0 )
					{
						cur = Integer.parseInt( cur_TF.getText() ) - 1;
						nGroup.curNum = cur;
						db.uploadUserInfo(uInfo);
					} else {
						db.stateMsgr.setText("不能再小了");
					}
				}
				if( cur >= 0 && cur <= max ) {
					cur_TF.setText( "" + cur );
					reLength();
					refresh();
				}
			}
		}
		
		private class KeyboardDetector extends KeyAdapter {
			private JTextField src;
			
			public KeyboardDetector(JTextField src) {
				this.src = src;
			}
			
			public void keyReleased(KeyEvent e) {
				int tmp = src.getText().length();
				if( tmp < 1 ) {
					tmp = 1;
				} else if( tmp >2 ) {
					src.setText(src.getText().substring(0, 2));
					db.stateMsgr.setText("欄位長度限制：2位數");
					tmp = 2;
				}
				src.setColumns(tmp);
				space.setColumns(4-cur_TF.getColumns()-max_TF.getColumns());
				refresh();
				src.requestFocus();
			}
		}
	}