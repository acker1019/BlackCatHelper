import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JLabel;


public class FileWalker {
	private JLabel h;
	public String[] getlisttmp(String path) {
		File f = new File(path);
		return f.list();
	}
	
	public String filterFileName(String input) {
		String[] buffer = input.split("/");
		return buffer[buffer.length-1];
	}
	
	public String[] getlist(String path) {
		this.h = h;
		ArrayList<String> tmp = new ArrayList<String>();
		String[] output;
		CodeSource src = getClass().getProtectionDomain().getCodeSource();
		try {
			if (src != null) {
				URL jar = src.getLocation();
				ZipInputStream zip = new ZipInputStream(jar.openStream());
				ZipEntry e;
				while( (e = zip.getNextEntry()) != null) {
					String name = e.getName();
					if (name.startsWith(path) && name.endsWith(".txt")) {
						tmp.add(filterFileName(name));
					}
				}
				output = tmp.toArray(new String[tmp.size()]);
				return output;
			}
		} catch (IOException e1) {
			// fail
		}
		return null;
	}
}
