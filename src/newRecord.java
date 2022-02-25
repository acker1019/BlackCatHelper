import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class newRecord extends JPanel {
	public newRecord src;
	public String identify;
	
	private innerDB db;
	public JComboBox<Object> selector;
	private RecordsPanel RecsP;
	
	private JButton addNewTab_btn;
	private BtnHandler bh;
	
	
	public newRecord(innerDB db, RecordsPanel RecsP ) {
		this.setName("+");
		this.src = this;
		identify = "+";
		
		this.db = db;
		this.selector = selector;
		this.RecsP = RecsP;
		
		bh = new BtnHandler();
		
		this.setLayout(new GridBagLayout());
		
		addNewTab_btn= new JButton("＋ 建立新標籤");
		addNewTab_btn.setFocusable(false);
		addNewTab_btn.addActionListener(bh);
		this.add(addNewTab_btn);
	}
	
	private class BtnHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Object event = e.getSource();
			if( event == addNewTab_btn ) {
				String newName = (String)JOptionPane.showInputDialog(
						src,
	                    "請輸入新的標籤名稱",
	                    "標籤名稱",
	                    JOptionPane.PLAIN_MESSAGE);
				if(newName != null && !newName.equals("")){
					selector.addItem(newName);
					db.createUserInfo(newName);
					UserInfo nInfo = db.getUserInfo(newName);
					db.uploadUserInfo(nInfo);
					Record nRec = RecsP.extendRecord(db, nInfo);
					RecsP.recrods.add(nRec);
					RecsP.addTab(nRec.getName(), nRec);
					RecsP.addTab(RecsP.newR.getName(), RecsP.newR);
					RecsP.setSelectedIndex(0);
					db.stateMsgr.setText("標籤 [" + newName + "] 已建立");
				} else {
					db.stateMsgr.setText("取消建立標籤");
				}
			}
		}
	} 
}
