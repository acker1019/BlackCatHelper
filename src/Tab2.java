import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.MutableComboBoxModel;


public class Tab2 extends Tab {
	private RecordsPanel RecsPl;
	
	private JTextField newname_TF;
	public JComboBox<Object> targets_c;
	private JButton delete_btn;
	private JButton rename_btn;
	
	private BtnHandler bh;
	private FocusHandler fh;
	
	private Tab2 src;
	
	public Tab2(innerDB db, RecordsPanel RecsPl) {
		super("�޲z", db);
		
		this.RecsPl = RecsPl;
		this.bh = new BtnHandler();
		this.fh = new FocusHandler();
		this.src = this;
		
		this.setLayout(new GridLayout(3, 1));
		
		JComponent pointer1;
		JComponent pointer2;
		
		pointer1 = new JPanel();
		pointer2 = new JLabel("[�޲z����]");
		pointer1.add(pointer2);
		this.add(pointer1);
		
		pointer1 = new JPanel();
		pointer2 = new JLabel("����ؼЪ����ҡG");
		pointer1.add(pointer2);
		
		ArrayList<String> name_tabs = new ArrayList<String>();
		for( Record r : RecsPl.recrods ) {
			name_tabs.add(r.getName());
		}
		targets_c = new JComboBox<Object>(name_tabs.toArray());
		targets_c.setPreferredSize(new Dimension(250, targets_c.getPreferredSize().height));
		pointer1.add(targets_c);
		
		pointer2 = new JPanel();
		pointer2.setPreferredSize(new Dimension(120, 0));
		pointer1.add(pointer2);
		
		delete_btn = new JButton("�R��");
		delete_btn.setFocusable(false);
		delete_btn.addActionListener(bh);
		pointer1.add(delete_btn);
		
		this.add(pointer1);
		
		pointer1 = new JPanel();
		newname_TF = new JTextField(10);
		newname_TF.addFocusListener(fh);
		pointer1.add(newname_TF);
		
		rename_btn = new JButton("��W");
		rename_btn.setFocusable(false);
		rename_btn.addActionListener(bh);
		pointer1.add(rename_btn);
		
		this.add(pointer1);
	}
	
	private class FocusHandler extends FocusAdapter {
		@Override
		public void focusGained(FocusEvent arg0) {
			newname_TF.selectAll();
		}
	}
	
	private class BtnHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(targets_c.getSelectedItem() == null) {
				db.stateMsgr.setText("�S������");
				return;
			}
			Object event = e.getSource();
			if( event == delete_btn ) {
				String targetName = (String) targets_c.getSelectedItem();
				int result = JOptionPane.showConfirmDialog(
						src,
					    "�A�u���n�R�� [" + targetName + "] ��?\n�R������ƵL�k�_��",
					    "�R���T�{",
					    JOptionPane.YES_NO_OPTION);
				if(result == 0) {
					int targetIndex = targets_c.getSelectedIndex();
					targets_c.removeItemAt(targetIndex);
					RecsPl.remove(targetIndex);
					RecsPl.recrods.remove(targetIndex);
					if(RecsPl.lastSelectedIndex == 0) {
						RecsPl.setSelectedIndex(0);
						RecsPl.lastSelectedIndex = 0;
						db.meta.lastTab = 0;
					} else if(RecsPl.lastSelectedIndex == RecsPl.recrods.size()) {
						RecsPl.setSelectedIndex(--RecsPl.lastSelectedIndex);
					}
					db.deleteUserInfo(targetIndex);
					db.uploadUserInfos();
					db.stateMsgr.setText("���� [" + targetName + "] �w�Q�R��");
				} else if(result == 1) {
					db.stateMsgr.setText("�����R������ [" + targetName + "]");
				}
			} else if( event == rename_btn ) {
				String newName = newname_TF.getText();
				if(newName.equals("")) {
					db.stateMsgr.setText("�п�J�s�W��");
					return;
				}
				String targetName = (String) targets_c.getSelectedItem();
				int result = JOptionPane.showConfirmDialog(
						src,
					    "�n�� [" + targetName + "] ��W�� [" + newName + "] ��?",
					    "��W�T�{",
					    JOptionPane.YES_NO_OPTION);
				if(newName.equals(targets_c.getSelectedItem())) {
					db.stateMsgr.setText("[" + newName + "] ���O�쥻���W�r�� �� �� ?");
					return;
				}
				Boolean checkName = true;
				for(int i = 0 ; i < targets_c.getItemCount() ; i++) {
					if(newName.equals(targets_c.getItemAt(i))) {
						checkName = false;
					}
				}
				if(!checkName) {
					db.stateMsgr.setText("[" + newName + "] �o�ӦW�٤w�g�s�b�F");
					return;
				}
				if(result == 0) {
					int targetIndex = targets_c.getSelectedIndex();
					targets_c.removeItemAt(targetIndex);
					targets_c.insertItemAt(newName, targetIndex);
					targets_c.setSelectedIndex(targetIndex);
					RecsPl.setTitleAt(targetIndex, newName);
					RecsPl.recrods.get(targetIndex).setName(newName);
					UserInfo u = RecsPl.recrods.get(targetIndex).uInfo;
					u.name = newName;
					db.uploadUserInfo(u);
					newname_TF.setText("");
					db.stateMsgr.setText("[" + targetName + "] �w�g�Q��W�� [" + newName + "] �F");
				} else {
					db.stateMsgr.setText("������W");
				}
			}
		}
	}
}
