package org.study.vnTokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Lop lay 100.000 van ban tu file CRAWLE_NEWS.txt 
 * de phan loai van ban.
 * 100.000 van ban nam trong file CRAWLE_NEWS_TOP_100K.txt
 * 
 * @created 19 / 02 / 2015
 * @author baonc
 *
 */
public class GeneratingFile {
	public void generatingFile() {
		String line = null;
		String enter = "\n";
		Path fileInput = Paths.get("data/CRAWLE_NEWS.txt");
		Path fileOutput = Paths.get("data/CRAWLE_NEWS_TOP_100K.txt");
		
		try(InputStream in = Files.newInputStream(fileInput);
				BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			OutputStream out = Files.newOutputStream(fileOutput);
			
			// doc 100.000 dong dau tien
			for(int i = 0; i < 100000; i++) {
				line = reader.readLine();
				System.out.println(line);
				byte data[] = line.getBytes();
				out.write(data);
				data = enter.getBytes();
				out.write(data);
			}
			
			in.close();
			out.close();
		} catch(IOException ioe) {
			System.err.println(ioe);
		}
	}
}
