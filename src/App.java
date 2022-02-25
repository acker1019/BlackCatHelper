import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.FontUIResource;


public class App extends JFrame{
	private innerDB db;
	private PreLoader preLoader;
	private eManager eM;
	private TabsPanel tabsPanel;
	private StateBar statebar;
	
	public App() {
		super("黑貓維茲素材管理輔助工具ver1.0");
		
		ImageIcon frameIcon = new ImageIcon(getClass().getResource("/BlackCat/0900.png"));
	    this.setIconImage(frameIcon.getImage() );
		
		winHandler wh = new winHandler();
		this.addWindowListener(wh);
		
		preLoader = new PreLoader();
		preLoader.downloadMeta();//預載Meta
		
		this.setSize(1125, 720);
		this.setMinimumSize(new Dimension(800, 500));
		this.setResizable(true);
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		this.setLocation(50, 50);
		this.setLayout(new BorderLayout());
		this.setVisible(true);
		
		this.setUIFont(new FontUIResource("新細明體", Font.PLAIN, preLoader.meta.fontSize));
		statebar = new StateBar();
		this.add(statebar, BorderLayout.SOUTH);
		
		db = new innerDB(statebar.state, preLoader);
		
		db.stateMsgr.setText("載入MetaInf  |  載入卡片資訊(100%)  |  排序卡片 |  建立索引  |  載入使用者資訊  |  載入UI  |  . . .");
		tabsPanel = new TabsPanel(db);
		eM = new eManager();
		tabsPanel.addEventManager(eM);
		add(tabsPanel, BorderLayout.CENTER);
		db.stateMsgr.setText("程式準備就緒   ||   自我介紹：我是狀態列..小列，那個..要幫忙你也不是不行啦≧Ｏ≦");
		db.f = this;
	}
	
	private class StateBar extends JPanel {
			public JLabel state;
			
			public StateBar() {
				this.setLayout(new BorderLayout());
				state = new JLabel();
				Border empty = BorderFactory.createEmptyBorder(5, 5, 5, 5);
				Border blackline = BorderFactory.createLineBorder(Color.gray);
				state.setBorder(BorderFactory.createCompoundBorder(blackline, empty));
				this.add(state, BorderLayout.CENTER);
			}
		}
	
	private class eManager implements EventManager {
		public void setFont(int size) {
			db.meta.fontSize = size;
			resetFontSize(size);
		}
	}
	
	public void resetFontSize(int n) {
		setUIFont(new FontUIResource("新細明體", Font.PLAIN, n));
		
		TabsPanel tmp = new TabsPanel(db);
		tmp.addEventManager(eM);
		tabsPanel.setVisible(false);
		this.remove(tabsPanel);
		tabsPanel = tmp;
		this.add(tabsPanel, BorderLayout.CENTER);
		tabsPanel.setSelectedIndex(2);
		
		StateBar tmpbar = new StateBar();
		statebar.setVisible(false);
		JLabel jbl = new JLabel();
		db.stateMsgr = jbl;
		this.remove(statebar);
		statebar = tmpbar;
		this.add(statebar, BorderLayout.SOUTH);
		db.stateMsgr = statebar.state;
	}
	
	public void setUIFont (FontUIResource fui){
		Enumeration keys=UIManager.getDefaults().keys();
			while (keys.hasMoreElements()) {
				Object key=keys.nextElement();
				Object value=UIManager.get(key);
				if (value != null && value instanceof FontUIResource) {
					UIManager.put(key, fui);
				}
			}
	}
	
	private class winHandler extends WindowAdapter {
		@Override
        public void windowClosing(WindowEvent e)
        {
			db.meta.lastTab = tabsPanel.tab1.recordsP.lastSelectedIndex;
			db.uploadMeta();
        }
	}
}
