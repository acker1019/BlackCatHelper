import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class TabsPanel extends JTabbedPane  {
	private Tab tab;
	public Tab1 tab1;
	private Tab2 tab2;
	private TabSettings tabSet;
	private TabAbout tabAbout;
	private innerDB db;
	
	public TabsPanel(innerDB db) {
		this.db = db;
		ChangeHandler ch = new ChangeHandler(this);
		this.addChangeListener(ch);
		this.setTabPlacement(JTabbedPane.RIGHT);
		tab1 = new Tab1(db);
		this.addTab( tab1.getName(), tab1 );
		tab2 = new Tab2(db, tab1.recordsP);
		tab1.recordsP.selector = tab2.targets_c;
		tab1.recordsP.newR.selector = tab2.targets_c;
		this.addTab( tab2.getName(), tab2 );
		tabSet = new TabSettings(db);
		this.addTab( tabSet.getName(), new Scrollpane(tabSet) );
		tabAbout = new TabAbout(db);
		this.addTab(tabAbout.getName(), new Scrollpane(tabAbout));
	}
	
	public void addEventManager(EventManager e) {
		tabSet.addEventManager(e);
	}
	
	public class ChangeHandler implements ChangeListener {
		private TabsPanel src;
		
		public ChangeHandler(TabsPanel src) {
			this.src = src;
		}
		
		@Override
		public void stateChanged(ChangeEvent arg) {
			String name = "";
			switch(src.getSelectedIndex()) {
				case 0:
					name = "素材   ||   素材統計表。可按上方「+」新增標籤，詳細使用說明請看「關於」";
					break;
				case 1:
					name = "管理   ||   刪除標籤、標籤改名";
					break;
				case 2:
					name = "設定   ||   喜好設定";
					break;
				case 3:
					name = "關於   ||   我是狀態列...小列，我不是傲嬌  !!ゞ(≧ε≦；)";
					break;
			}
			db.stateMsgr.setText(name);
		}
	}
}
