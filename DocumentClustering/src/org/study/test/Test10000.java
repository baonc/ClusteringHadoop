package org.study.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Lop chon ra 10.000 VB de test
 * 
 * @created 5 / 4 / 2010
 * @author baonc
 *
 */
public class Test10000 {
	public void createdFile() {
		Path file = Paths.get("/home/baonc/Desktop/Dimension/part-r-00000");
		String line = null;
		String enter = "\n";
		
		try(InputStream in = Files.newInputStream(file);
				BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			OutputStream out = Files.newOutputStream(Paths.get("/home/baonc/Desktop/Dimension/top10000"));

			for(int i = 0; i < 10000; i++) {
				line = reader.readLine();
				byte data[] = line.getBytes();
				out.write(data);
				data = enter.getBytes();
				out.write(data);
			}
			
			in.close();
			out.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
