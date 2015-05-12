package org.study.vnTokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Lop chia file lon ra thanh nhung file nho luu tru trong hdfs
 * 
 * @created 3 / 3 / 2015
 * @author baonc
 *
 */
public class DivideDocuments {
	
	public void divide() {
		String line = null;
		
		try(InputStream in = Files.newInputStream(Paths.get("data/CRAWLE_NEWS_TOP_100K_VIET_TOKEN.txt"));
				BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			int documents = 1;
			while((line = reader.readLine()) != null) {
				System.out.println("Reading file " + documents);
				OutputStream out = Files.newOutputStream(Paths.get("data/documents/" + documents));
				byte data[] = line.getBytes();
				out.write(data);
				out.close();
				documents++;
			}
			System.out.println("done.");
			reader.close();
			in.close();
		} catch(IOException ioe) {
			System.err.print(ioe);
		}
	}
}
