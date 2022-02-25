import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.MaskFormatter;


public class Record extends JPanel {
	private final int max_height = 12;
	private final Record thisRecord = this;
	
	public int border_top;
	public int border_left;
	public int border_bottom;
	public int border_right;
	
	private ImgSelector selector;
	private innerDB db;
	public ArrayList<RItem> items;
	public UserInfo uInfo;
	public ColorPack Cpack;
	
	private ToolsPanel tools;
	private JPanel container;
	public Area areaL;
	public Area areaM;
	public Area areaR;
	
	public ArrayList<RItem> itemsL;
	public ArrayList<RItem> itemsM;
	public ArrayList<RItem> itemsR;
	
	private ArrayList<RItem> onDisplay_itemsL;
	private ArrayList<RItem> onDisplay_itemsM;
	private ArrayList<RItem> onDisplay_itemsR;
	
	public Record(innerDB db, UserInfo uInfo, ColorPack Cpack) {
		this.setName(uInfo.name);
		this.setLayout(new BorderLayout());
		this.db = db;
		this.uInfo = uInfo;
		this.Cpack = Cpack;
		items = new ArrayList<RItem>();
		itemsL =  new ArrayList<RItem>();
		itemsM =  new ArrayList<RItem>();
		itemsR =  new ArrayList<RItem>();
		border_top = 5;
		border_left = 20;
		border_right = 20;
		border_bottom = 25;
		this.setBorder(BorderFactory.createMatteBorder(border_top, border_left, border_bottom, border_right, Color.white));
		
		tools = new ToolsPanel();
		this.add(tools, BorderLayout.NORTH);
		container = new JPanel();
		container.setLayout(new GridLayout(1, 3));
		this.add(new Scrollpane(container), BorderLayout.CENTER);
		
		ArrayList<Data> query = db.queryByIndex("fire");
		for(Data d : query) {
			NumGroup g = uInfo.getNumGroup(d.id);
			this.addItem(d.id, d.img, "left", g, uInfo);
		}
		
		query = db.queryByIndex("water");
		for(Data d : query) {
			NumGroup g = uInfo.getNumGroup(d.id);
			this.addItem(d.id, d.img, "mid", g, uInfo);
		}
		
		query = db.queryByIndex("bolt");
		for(Data d : query) {
			NumGroup g = uInfo.getNumGroup(d.id);
			this.addItem(d.id, d.img, "right", g, uInfo);
		}
		
		refreshLayout();
	}
	
	public void refreshLayout() {
		int finalHirght = 0;
		int vtmp = -1;
		onDisplay_itemsL = new ArrayList<RItem>();
		for(RItem item : itemsL) {
			if(item.nGroup.dispaly) {
				onDisplay_itemsL.add(item);
			}
		}
		vtmp = onDisplay_itemsL.size();
		finalHirght = (vtmp > finalHirght) ? vtmp : finalHirght;
		onDisplay_itemsM = new ArrayList<RItem>();
		for(RItem item : itemsM) {
			if(item.nGroup.dispaly) {
				onDisplay_itemsM.add(item);
			}
		}
		vtmp = onDisplay_itemsM.size();
		finalHirght = (vtmp > finalHirght) ? vtmp : finalHirght;
		onDisplay_itemsR = new ArrayList<RItem>();
		for(RItem item : itemsR) {
			if(item.nGroup.dispaly) {
				onDisplay_itemsR.add(item);
			}
		}
		vtmp = onDisplay_itemsR.size();
		finalHirght = (vtmp > finalHirght) ? vtmp : finalHirght;
		
		container.setVisible(false);
		container.removeAll();
		areaL = new Area(finalHirght);
		placeRItem(areaL, onDisplay_itemsL);
		container.add(areaL);
		areaM = new Area(finalHirght);
		placeRItem(areaM, onDisplay_itemsM);
		container.add(areaM);
		areaR = new Area(finalHirght);
		placeRItem(areaR, onDisplay_itemsR);
		container.add(areaR);
		container.setVisible(true);
	}
	
	public void placeRItem(Area area, ArrayList<RItem> ritems) {
		for(RItem items : ritems) {
			area.add(items);
		}
	}
	
	public void addItem(String id, ImageIcon img, String pos, NumGroup nGroup, UserInfo uInfo) {
		RItem item = new RItem(id, img, db, Cpack.items, nGroup, uInfo);
		items.add(item);
		switch(pos) {
			case "left":
				itemsL.add(item);
				break;
			case "mid":
				itemsM.add(item);
				break;
			case "right":
				itemsR.add(item);
				break;
		}
	}
	
	private class ToolsPanel extends JPanel {
		private JButton edit;
		private JButton submit;
		private JButton select;
		private BtnHandler h;
		
		public ToolsPanel() {
			this.setBackground(Cpack.title);
			
			h = new BtnHandler();
			
			edit = new JButton("編輯");
			edit.addActionListener(h);
			this.add(edit);
			
			submit = new JButton("確認");
			submit.addActionListener(h);
			submit.setVisible(false);
			this.add(submit);
			
			select = new JButton("顯示設定");
			select.addActionListener(h);
			this.add(select);
		}
		
		private class BtnHandler implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				Object event = e.getSource();
				if( event == edit ) {
					if(onDisplay_itemsL.size()+onDisplay_itemsM.size()+onDisplay_itemsR.size() == 0) {
						db.stateMsgr.setText("提醒   ||   你還沒選擇卡片喔");
					} else {
						submit.setVisible(true);
						edit.setVisible(false);
						for( RItem i : items ) {
							i.max_TF.setEditable(true);
							i.max_TF.setBackground(Color.white);
							i.cur_TF.setEditable(true);
							i.cur_TF.setBackground(Color.white);
							i.minus.setVisible(true);
						}
						items.get(0).cur_TF.requestFocus();
						items.get(0).cur_TF.selectAll();
						db.stateMsgr.setText("已啟用編輯   ||   格式範例：( [現有卡片數量] / [卡片總需求量] )  ;     Ex. (11 /24 )");
					}
				} else if( event == submit ) {
					Boolean itsOK = true;
					int t_max = -1;
					int t_cur = -1;
					for( RItem i : items ) {
						t_max = Integer.parseInt( i.max_TF.getText() );
						t_cur = Integer.parseInt( i.cur_TF.getText() );
						if( t_cur > t_max ) {
							itsOK = false;
							break;
						}
					}
					if( itsOK ) {
						submit.setVisible(false);
						edit.setVisible(true);
						for( RItem i : items ) {
							i.max_TF.setEditable(false);
							i.max_TF.setBackground(Cpack.items);
							i.cur_TF.setEditable(false);
							i.cur_TF.setBackground(Cpack.items);
							i.max = Integer.parseInt( i.max_TF.getText() );
							i.cur = Integer.parseInt( i.cur_TF.getText() );
							i.nGroup.tolNeeded = i.max;
							i.nGroup.curNum = i.cur;
							i.minus.setVisible(false);
						}
						db.uploadUserInfo(uInfo);
						db.stateMsgr.setText("編輯完成");
					} else {
						db.stateMsgr.setText("輸入格式錯誤   ||   現存卡數不能大於總需求量");
					}
				} else if( event == select ) {
					if( selector == null ) {
						selector = new ImgSelector(thisRecord, uInfo, db);
					}
					selector.setVisible(true);
				}
			}
		}
	}
	
	public class Area extends JPanel{
		public Area(int hight) {
			this.setBackground(Color.white);
			if(hight<8) {
				hight = 8;
			}
			this.setLayout(new GridLayout(hight, 1));
		}
	}
}
