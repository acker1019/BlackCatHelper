import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class PreLoader {
	public MetaInfo meta;
	
	public PreLoader() {
		meta = new MetaInfo();
	}
	
	public void downloadMeta() {
		File f = new File("inf/meta.txt");
		if(f.exists()) {
			FileReader fr;
			try {
				fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				String newLine = br.readLine();
				meta.fontSize = Integer.parseInt(newLine);
				newLine = br.readLine();
				meta.imgSize = Integer.parseInt(newLine);
				newLine = br.readLine();
				meta.isResizeIcon = Boolean.parseBoolean(newLine);
				newLine = br.readLine();
				meta.lastTab = Integer.parseInt(newLine);
				br.close();
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			LoadDefaultMeta();
		}
	}
	
	public void LoadDefaultMeta() {
		try {
			InputStream is = this.getClass().getResourceAsStream("/BlackCat/meta.txt");
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader bf = new BufferedReader(isr);
			String newLine = bf.readLine();
			meta.fontSize = Integer.parseInt(newLine);
			newLine = bf.readLine();
			meta.imgSize = Integer.parseInt(newLine);
			newLine = bf.readLine();
			meta.isResizeIcon = Boolean.parseBoolean(newLine);
			newLine = bf.readLine();
			meta.lastTab = Integer.parseInt(newLine);
			bf.close();
			isr.close();
			is.close();
		} catch (IOException e) {
			// for state bar
			e.printStackTrace();
		}
	}
	
	public void uploadMeta() {
		try {
			File f = new File("inf/meta.txt");
			if(!f.exists()) {
				f.createNewFile();
			}
			FileWriter fw = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(meta.fontSize + "\r\n");
			bw.write(meta.imgSize + "\r\n");
			bw.write(meta.isResizeIcon + "\r\n");
			bw.write("" + meta.lastTab);
			bw.close();
			fw.close();
		} catch (IOException e) {
			// for state bar
			e.printStackTrace();
		}
	}
}
