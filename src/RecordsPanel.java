import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class RecordsPanel extends JTabbedPane {
	private innerDB db;
	public ArrayList<Record> recrods;
	private ChangeHandler ch;
	public int lastSelectedIndex;
	public JComboBox<Object> selector;
	public newRecord newR;
	private ArrayList<ColorPack> Cpacks;
	private ArrayList<ColorPack> Cbuffer;
	private ColorPack remainder;
	
	public RecordsPanel(innerDB db) {
		remainder = null;
		lastSelectedIndex = -1;
		this.db = db;
		ch = new ChangeHandler(this);
		this.addChangeListener(ch);
		this.setTabPlacement(JTabbedPane.TOP);
		recrods = new ArrayList<Record>();
		Cbuffer = new ArrayList<ColorPack>();
		
		int counter = 0;
		Cpacks = new ArrayList<ColorPack>();
		Cpacks.add(new ColorPack(new Color(0, 170, 238), new Color(162, 216, 238), counter++));//blue
		Cpacks.add(new ColorPack(new Color(254, 170, 43), new Color(255, 202, 141), counter++));//orange
		Cpacks.add(new ColorPack(new Color(100, 189, 151), new Color(194, 255, 187), counter++));//green
		
		for( UserInfo info : db.userInfos) {
			Record tmp = extendRecord(db, info);
			recrods.add(tmp);
		}
		
		for( Record r : recrods ) {
			this.addTab( r.getName(), r);
		}
		
		newR = new newRecord(db, this);
		this.addTab(newR.getName(), newR);
		
		int pointer = db.meta.lastTab;
		if( pointer < 0 ) {
			pointer = 0;
		}
		this.setSelectedIndex(pointer);
	}
	
	public Record extendRecord(innerDB db, UserInfo info) {
		Random ran = new Random();
		int index;
		Boolean reloaded = false;
		if(Cbuffer.isEmpty()) {
			reloaded = true;
			for( ColorPack p : Cpacks ) {
				if(remainder == null || p.id != remainder.id) {
					Cbuffer.add(p);
				}
			}
		} else if(Cbuffer.size() == 1) {
			remainder = Cbuffer.get(0);
		}
		index = ran.nextInt(Cbuffer.size());
		ColorPack p = Cbuffer.get(index);
		Cbuffer.remove(index);
		if(reloaded && remainder != null) {
			Cbuffer.add(remainder);
			remainder = null;
		}
		return new Record(db, info, p);
	}
	
	public class ChangeHandler implements ChangeListener {
		private RecordsPanel src;
		
		public ChangeHandler(RecordsPanel src) {
			this.src = src;
		}
		
		@Override
		public void stateChanged(ChangeEvent arg) {
			int index = src.getSelectedIndex();
			if(src.getSelectedComponent().getName().equals("+") && index == 0) {
				return;
			}
			if(recrods.size()-1 < index) {
				String newName = (String)JOptionPane.showInputDialog(
						src,
	                    "請輸入新的標籤名稱",
	                    "標籤名稱",
	                    JOptionPane.PLAIN_MESSAGE);
				if(newName != null && !newName.equals("")){
					Boolean checkName = true;
					for(int i = 0 ; i < selector.getItemCount() ; i++) {
						if(newName.equals(selector.getItemAt(i))) {
							checkName = false;
						}
					}
					if(!checkName) {
						db.stateMsgr.setText("[" + newName + "] 這個名稱已經存在了");
						src.setSelectedIndex(lastSelectedIndex);
						db.meta.lastTab = lastSelectedIndex;
						return;
					}
					selector.addItem(newName);
					db.createUserInfo(newName);
					UserInfo nInfo = db.getUserInfo(newName);
					db.uploadUserInfo(nInfo);
					Record nRec = extendRecord(db, nInfo);
					recrods.add(nRec);
					src.addTab(nRec.getName(), nRec);
					src.addTab(newR.getName(), newR);
					lastSelectedIndex = recrods.size()-1;
					src.setSelectedIndex(lastSelectedIndex);
					db.stateMsgr.setText("標籤 [" + newName + "] 已建立");
				} else {
					src.setSelectedIndex(lastSelectedIndex);
					db.meta.lastTab = lastSelectedIndex;
					db.stateMsgr.setText("取消建立標籤");
				}
			} else {
				lastSelectedIndex = index;
			}
		}
	}
}
