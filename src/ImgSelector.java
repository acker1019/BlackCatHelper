import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class ImgSelector extends JFrame {
	private Record src;
	private UserInfo uInfo;
	private innerDB db;
	private WinFHandler wfh;
	
	public ImgSelector(Record src, UserInfo uInfo, innerDB db) {
		super("[" + src.getName() + "] - 選擇要顯示的素材");
		this.setSize(520, 700);
		this.setMinimumSize(new Dimension(470, 500));
		this.setResizable(true);
		this.setLocation(800, 120);
		this.setLayout(new BorderLayout());
		
		this.src = src;
		this.uInfo = uInfo;
		this.db = db;
		
		wfh = new WinFHandler();
		this.addWindowFocusListener(wfh);
		
		this.add(new Scrollpane( new MainPanel() ), BorderLayout.CENTER);
	}
	
	private class WinFHandler implements WindowFocusListener {
		@Override
		public void windowGainedFocus(WindowEvent e) {
			db.stateMsgr.setText("[" + src.getName() + "] - 選擇要顯示的素材");
		}

		@Override
		public void windowLostFocus(WindowEvent arg0) {
			//No
		}
	}
	
	private class MainPanel extends JPanel {
		private ArrayList<Item> items;
		
		public Area areaTop;
		public Area areaMid;
		public Area areaBottom;
		
		private Bar curBar;
		
		public MainPanel() {
			items = new ArrayList<Item>();
			this.setLayout(new GridLayout(3, 1));
			areaTop = new Area();
			this.add(areaTop);
			areaMid = new Area();
			this.add(areaMid);
			areaBottom = new Area();
			this.add(areaBottom);
			
			ArrayList<Data> query = db.queryByIndex("fire");
			for(Data d : query) {
				this.addItem(d.id, d.img, d.img_lowlight, "top", trans(d.race), new Color(255, 202, 141));
			}
			
			query = db.queryByIndex("water");
			for(Data d : query) {
				this.addItem(d.id, d.img, d.img_lowlight, "mid", trans(d.race), new Color(154, 232, 255));
			}
			
			query = db.queryByIndex("bolt");
			for(Data d : query) {
				this.addItem(d.id, d.img, d.img_lowlight, "bottom", trans(d.race), new Color(243, 237, 146));
			}
		}
		
		public int trans(String in) {
			switch(in) {
				case "pongee":
					return 0;
				case "flower":
					return 1;
				case "mushroom":
					return 2;
				case "tree":
					return 3;
				case "RacconDog":
					return 4;
				default:
						return -1;
			}
		}
		
		private void addItem(String id, ImageIcon img, ImageIcon img_lowlight, String pos, int barNum, Color bg) {
			NumGroup g = uInfo.getNumGroup(id);
			Item item = new Item(id, img, img_lowlight, bg, g.dispaly);
			items.add(item);
			switch(pos) {
				case "top":
					areaTop.bars.get(barNum).add(item);
					break;
				case "mid":
					areaMid.bars.get(barNum).add(item);
					break;
				case "bottom":
					areaBottom.bars.get(barNum).add(item);
					break;
			}
		}
		
		private class Item extends JPanel {
			public String id;
			public ImgBtn img;
			public ImgBtn img_lowlight;
			private Color bg;
			private RItem refItem;
			
			private ImgHandler h;
			
			public Item(String id, ImageIcon img, ImageIcon img_lowlight, Color bg, Boolean iniVisiable) {
				this.bg = bg;
				this.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, bg));
				this.setBackground(bg);
				this.id = id;
				
				for( RItem i : src.items ) {
					if( this.id.equals(i.id) ) {
						this.refItem = i;
						break;
					}
				}
				
				h = new ImgHandler();
				
				this.img = new ImgBtn(img);
				this.img.addActionListener(h);
				this.img.setVisible(false);
				this.add(this.img);
				
				this.img_lowlight = new ImgBtn(img_lowlight);
				this.img_lowlight.addActionListener(h);
				this.add(this.img_lowlight);
				
				if(iniVisiable) {
					switchBtn();
				}
			}
			
			private void switchBtn() {
				if(img.isVisible()) {
					img.setVisible(false);
					img_lowlight.setVisible(true);
				} else {
					img_lowlight.setVisible(false);
					img.setVisible(true);
				}
			}
			
			private class ImgBtn extends JButton {
				public ImgBtn(ImageIcon img) {
					super(img);
					this.setFocusable(false);
					this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					this.setPreferredSize(new Dimension(img.getIconHeight(),img.getIconWidth()));
				}
			}
			
			private class ImgHandler implements ActionListener {
				public void actionPerformed(ActionEvent e) {
					Object event = e.getSource();
					if( event == img ) {
						refItem.setVisible(false);
						db.stateMsgr.setText("卡片移除   ||   [卡片編號:" + id + "]");
					} else if( event == img_lowlight ) {
						refItem.setVisible(true);
						db.stateMsgr.setText("卡片新增   ||   [卡片編號:" + id + "]");
					}
					switchBtn();
					if( refItem.nGroup.cardID == null ) {
						refItem.nGroup.cardID = id;
					}
					refItem.nGroup.dispaly = img.isVisible();
					db.uploadUserInfo(uInfo);
					src.refreshLayout();
				}
			}
		}
		
		private class Bar extends JPanel{
			public Bar() {
				this.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.white));
				this.setLayout(new GridLayout(1, 5));
			}
		}
		
		private class Area extends JPanel{
			public ArrayList<Bar> bars;
			
			public Area() {
				this.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, Color.white));
				this.setLayout(new GridLayout(5, 1));
				bars = new ArrayList<Bar>();
				for(int i = 0 ; i < 5 ; i++) {
					bars.add(new Bar());
					this.add( bars.get(i) );
				}
			}
		}
	}
}
