import javax.swing.ImageIcon;


public class NumGroup {
		public String cardID;
		public int tolNeeded;
		public int curNum;
		public Boolean dispaly;
		
		public NumGroup(String cardID, int tolNeeded) {
			this.cardID = cardID;
			this.tolNeeded = tolNeeded;
			this.curNum = 0;
			this.dispaly = true;
		}
		
		public NumGroup(String cardID, int tolNeeded, int curNum, Boolean dispaly) {
			this.cardID = cardID;
			this.tolNeeded = tolNeeded;
			this.curNum = curNum;
			this.dispaly = dispaly;
		}
	}