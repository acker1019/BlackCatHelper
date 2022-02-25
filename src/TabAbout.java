import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class TabAbout extends Tab {
	public TabAbout(innerDB db) {
		super("關於", db);
		
		this.setLayout(new BorderLayout());
		
		JComponent pointer1;
		JTextArea pointer2;
		
		pointer1 = new JPanel();
		pointer2 = new JTextArea();
		String content = 
				"[黑貓維茲素材管理輔助工具]\n"
				+ "版本：Ver1.0\n"
				+ "完成日期：2015/8/16\n"
				+ "圖片取自黑貓wiki\n"
				+ "聲明：\n"
				+ "本人因自己和朋友計算黑貓素材的需求\n"
				+ "故設計此程式增加便利性\n"
				+ "不做商業用途\n"
				+ "Copyright © 2015 R禾\n\n"
				+ "===============使用說明===============\n"
				+ " 1. 選擇一個標籤，若沒有，請新增\n"
				+ " 2. 點擊「顯示設定」選擇要顯示的素材卡片\n"
				+ " 3. 點擊「編輯」設定卡片數量\n"
				+ "      格式範例： ( [現有的數量] / [總共需要的數量] )\n"
				+ "      Ex.  ( 11 / 24 )   <--  需要24張卡片，現在打了11張\n"
				+" 4. 點擊「確認」完成輸入\n"
				+" 5. 使用「＋」號記錄卡片數量\n"
				+ " ps. 每個標籤的圖片選擇視窗是獨立的";
		pointer2.setText(content);
		pointer2.setEditable(false);
		pointer1.add(pointer2);
		
		this.add(pointer2, BorderLayout.CENTER);
	}
}
