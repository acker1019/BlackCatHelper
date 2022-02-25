import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class innerDB {
	private ArrayList<Data>  materials;
	private ArrayList<Index> indexList;
	public ArrayList<UserInfo> userInfos;
	public MetaInfo meta;
	public PreLoader preloader;
	
	private String[] chips;
	private ImageIcon img_tmp;
	private ImageIcon img_lowlight_tmp;
	
	public JLabel stateMsgr;
	
	public JFrame f;
	
	public innerDB(JLabel stateMsgr, PreLoader preloader) {
		this.stateMsgr = stateMsgr;
		this.preloader = preloader;
		meta = preloader.meta;
		materials = new ArrayList<Data>();
		indexList = new ArrayList<Index>();
		userInfos = new ArrayList<UserInfo>();
		chips = null;
		img_tmp = null;
		createInfSpace();
		preloader.uploadMeta();
		stateMsgr.setText("載入MetaInf  |  載入卡片資訊(0%)  |  . . .");
		loadData();
		stateMsgr.setText("載入MetaInf  |  載入卡片資訊(100%)  |  排序卡片 |  . . .");
		sortData();
		stateMsgr.setText("載入MetaInf  |  載入卡片資訊(100%)  |  排序卡片  |  建立索引  |  . . .");
		buildIndex();
		stateMsgr.setText("載入MetaInf  |  載入卡片資訊(100%)  |  排序卡片  |  建立索引  |  載入使用者資訊  |  . . .");
		downloadUserInfo_Def();
	}
	
	private void loadData() {
		try {
			InputStream is = innerDB.class.getResourceAsStream("/BlackCat/dataInfo.txt");
			InputStreamReader irs = new InputStreamReader(is);
			BufferedReader bf = new BufferedReader(irs);
			ArrayList<String> buffer = new ArrayList<String>();
			String newline;
			while( ( newline = bf.readLine() ) != null ) {
				buffer.add(newline);
			}
			bf.close();
			irs.close();
			is.close();
			int amg = buffer.size();
			int cur = 0;
			for( String s_newLine : buffer ) {
				chips = s_newLine.split("\\s");
				if( chips.length != 4 ) {
					continue;
				}
				img_tmp = new ImageIcon( innerDB.class.getResource("/BlackCat/img/" + chips[0] + ".png") );
				img_lowlight_tmp = new ImageIcon( innerDB.class.getResource("/BlackCat/img_lowlight/" + chips[0] + "_lowlight.png") );
				if(meta.isResizeIcon) {
					img_tmp = resizeImgIcon(img_tmp, meta.imgSize);
					img_lowlight_tmp = resizeImgIcon(img_lowlight_tmp, meta.imgSize);
				}
				Data data_tmp = new Data(chips[0], chips[1], chips[2], Integer.parseInt(chips[3]), img_tmp, img_lowlight_tmp);
				materials.add( data_tmp );
				stateMsgr.setText("載入MetaInf  |  載入卡片資訊(" + (++cur)*100/amg + "%)  |  . . .");
			}
		} catch (IOException e) {
			this.stateMsgr.setText("資料讀取錯誤，請重新執行程式");
			JOptionPane.showConfirmDialog(
					f,
				    "資料讀取錯誤，請重新執行程式",
				    "資料讀取錯誤",
				    JOptionPane.OK_OPTION);
			System.exit(1);
		}
	}
	
	public void uploadMeta() {
		preloader.uploadMeta();
	}
	
	public ImageIcon resizeImgIcon(ImageIcon src_imgicon, int size) {
		Image img = src_imgicon.getImage();
		img = img.getScaledInstance(size,size,java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(img);
	}
	
	public void resizeAllIcons(int size) {
		int i = 0;
		for( Data d : materials ) {
			img_tmp = new ImageIcon( innerDB.class.getResource("/BlackCat/img/" + d.id + ".png") );
			d.img = resizeImgIcon(img_tmp, size);
			img_lowlight_tmp = new ImageIcon( innerDB.class.getResource("/BlackCat/img_lowlight/" + d.id + "_lowlight.png") );
			d.img_lowlight = resizeImgIcon(img_lowlight_tmp, size);
			stateMsgr.setText("更新meta資訊  |  重新運算圖片(" + (i++)*100/materials.size() + "%)  |  . . .");
		}
	}

	private void createInfSpace() {
		File f = new File("inf");
		if( !f.exists() ) {
			f.mkdirs();
		}
	}
	
	private void buildIndex() {
		for(Data d : materials) {
			for(int i = 0 ; i <= 2 ; i++) {
				Boolean exsited = false;
				for( Index index : indexList ) {
					if( d.get(i).equals( index.title ) ) {
						exsited = true;
						break;
					}
				}
				if(!exsited) {
					indexList.add( new Index(d.get(i)) );
				}
				for( Index index : indexList ) {
					if( index.title.equals(d.get(i)) ) {
						index.data.add(d);
						break;
					}
				}
			}
		}
	}
	
	private void sortData() {
		materials = sorting(0, materials);
		for(Data data_tmp : materials) {
		}
	}
	
	private ArrayList<Data> sorting(int hierarchy, ArrayList<Data> remainder) {
		ArrayList<Index> objs;
		if(hierarchy > 2) {
			return remainder;
		} else {
			objs = new ArrayList<Index>();
			for( Data d : remainder ) {
				Boolean existed = false;
				String cur = d.get(hierarchy);
				for( Index i : objs ) {
					if(i.title.equals(cur)) {
						existed = true;
						break;
					}
				}
				if(!existed) {
					objs.add(new Index(cur));
				}
				for( Index i : objs ) {
					if(i.title.equals(cur)) {
						i.data.add(d);
						break;
					}
				}
			}
			ArrayList<Index> tmp = new ArrayList<Index>();
			while(objs.size() > 0) {
				Index max = new Index("");
				for(Index i : objs) {
					if(transWeight(i.title)>transWeight(max.title)) {
						max = i;
					}
				}
				tmp.add(max);
				objs.remove(max);
			}
			objs = tmp;
		}
		hierarchy++;
		ArrayList<Data> output = new ArrayList<Data>();
		for(Index i : objs) {
			ArrayList<Data> feedback = sorting(hierarchy, i.data);
			for(Data d : feedback) {
				output.add(d);
			}
		}
		return output;
	}
	
	public ArrayList<Data> queryByDB(String col, String target) {
		int colv = -1;
		ArrayList<Data> output = new ArrayList<Data>();
		switch(col) {
			case "attribute":
				colv = 0;
				break;
			case "race":
				colv = 1;
				break;
			case "lv":
				colv = 2;
				break;
			case "id":
				colv = 3;
				break;
		}
		for( Data d : materials ) {
			if(target.equals(d.get(colv))) {
				output.add(d);
			}
		}
		return output;
	}
	
	public ArrayList<Data> queryCross(String type1, String type2) {
		ArrayList<Data> src1 = queryByIndex(type1);
		ArrayList<Data> src2 = queryByIndex(type2);
		ArrayList<Data> output = new ArrayList<Data>();
		for( Data d1 : src1 ) {
			for( Data d2 : src2 ) {
				if( d1.id.equals(d2.id) ) {
					output.add(d1);
				}
			}
		}
		return output;
	}
	
	public ArrayList<Data> queryByIndex(String type) {
		ArrayList<Data> output = null;
		for( Index index : indexList ) {
			if( index.title.equals(type) ) {
				output = index.data;
				break;
			}
		}
		return output;
	}

	public void uploadUserInfos() {
		try {
			File dir = new File("inf/UserInfos");
			if( !dir.exists() ){
				dir.mkdirs();
			}
			for( UserInfo u : userInfos ) {
				File f = new File("inf/UserInfos/" + u.fileName);
				f.delete();
			}
			int t = 1;
			for( UserInfo u : userInfos ) {
				String newFileName = "t" + (t++) + ".txt";
				u.fileName = newFileName;
				File f = new File("inf/UserInfos/" + newFileName);
				if( f.exists() ){
					f.delete();
				}
				f.createNewFile();
				FileWriter fw = new FileWriter( f );
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(u.name + "\r\n");
				for( NumGroup g : u.data ) {
					if(g.cardID != null) {
						bw.write(g.cardID + " " + g.tolNeeded + " " + g.curNum + " " +String.valueOf(g.dispaly) + "\r\n");
					}
				}
				bw.close();
				fw.close();
			}
		} catch (IOException e) {
			// for state bar
			e.printStackTrace();
		}
	}
	
	public void uploadUserInfo(UserInfo uInfo) {
		try {
			File f = new File("inf/UserInfos/" + uInfo.fileName);
			if( f.exists() ){
				f.delete();
			}
			f.createNewFile();
			FileWriter fw = new FileWriter( f );
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(uInfo.name + "\r\n");
			for( NumGroup g : uInfo.data ) {
				if(g.cardID != null) {
					bw.write(g.cardID + " " + g.tolNeeded + " " + g.curNum + " " +String.valueOf(g.dispaly) + "\r\n");
				}
			}
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void downloadUserInfos() {
		try {
			File dir = new File("inf/UserInfos");
			if( dir.exists() ){
				String[] files = dir.list();
				for( String file : files ) {
					FileReader fr = new FileReader("inf/UserInfos/" + file);
					BufferedReader br = new BufferedReader(fr);
					UserInfo u = new UserInfo( br.readLine(), file);
					String newline;
					while( ( newline = br.readLine() ) != null ) {
						chips = newline.split("\\s");
						ImageIcon tmp_Img = queryByDB("id", chips[0]).get(0).img;
						u.data.add( new NumGroup( chips[0], Integer.parseInt(chips[1]), Integer.parseInt(chips[2]), Boolean.parseBoolean(chips[3])) );
					}
					userInfos.add(u);
					br.close();
					fr.close();
				}
			}
		} catch (IOException e) {
			// for state bar
			e.printStackTrace();
		}
	}
	
	public void downloadUserInfo_Def() {
		//destination
		File f = new File("inf/UserInfos");
		if( !f.exists() ) {
			//source
			FileWalker walker = new FileWalker();
			String[] fileNames = walker.getlist("BlackCat/UserInfos/");
			//String[] fileNames = walker.getlisttmp(getClass().getResource("/BlackCat/UserInfos").getFile() );
			userInfos.clear();
			try {
				for( String s : fileNames ) {
					InputStream is = getClass().getResourceAsStream("/BlackCat/UserInfos/" + s);
					
					InputStreamReader isr = new InputStreamReader(is);
					BufferedReader bf = new BufferedReader(isr);
					UserInfo u = new UserInfo( bf.readLine(), s );
					String newline;
					while( ( newline = bf.readLine() ) != null ) {
						chips = newline.split("\\s");
						ImageIcon tmp_Img = queryByDB("id", chips[0]).get(0).img;
						u.data.add( new NumGroup( chips[0], Integer.parseInt(chips[1]), Integer.parseInt(chips[2]), Boolean.parseBoolean(chips[3])) );
					}
					userInfos.add(u);
					bf.close();
					isr.close();
					is.close();
				}
				uploadUserInfos();
			} catch (IOException e) {
				stateMsgr.setText("使用者資訊讀取失敗");
			}
		} else {
			downloadUserInfos();
		}
	}
	
	public void createUserInfo(String name) {
		userInfos.add(new UserInfo(name, "t" + (userInfos.size()+1) + ".txt"));
		uploadUserInfo(userInfos.get(userInfos.size()-1));
	}
	
	public void deleteUserInfo(int i) {
		File f = new File("inf/UserInfos/" + userInfos.get(i).fileName);
		if( f.exists() ){
			f.delete();
		}
		userInfos.remove(i);
	}
	
	public UserInfo getUserInfo(String userName) {
		for(UserInfo info : userInfos  ) {
			if( userName.equals(info.name) ) {
				return info;
			}
		}
		return null;
	}
	
	public void setUserInfo(String userName, String cardID, String tolNeeded, String curNum) {
		UserInfo info = getUserInfo(userName);
		for( NumGroup g : info.data ) {
			if( g.cardID.equals(cardID) ) {
				g.tolNeeded = Integer.parseInt(tolNeeded);
				g.curNum = Integer.parseInt(curNum);
				return;
			}
		}
	}
	
	public int transWeight(String in) {
		switch(in) {
			case "fire":
				return 300;
			case "water":
				return 200;
			case "bolt":
				return 100;
			case "pongee":
				return 50;
			case "flower":
				return 40;
			case "mushroom":
				return 30;
			case "tree":
				return 20;
			case "RacconDog":
				return 10;
			case "1":
				return 5;
			case "2":
				return 4;
			case "3":
				return 3;
			case "4":
				return 2;
			case "5":
				return 1;
			default:
				return -1;
		}
	}
	
	//--below-- [Custom data type]
	private class Index {
		public String title;
		public ArrayList<Data> data;
		
		public Index(String title) {
			this.title = title;
			data = new ArrayList<Data>();
		}
	}
}
