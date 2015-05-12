package org.study.test;

import static java.nio.file.StandardOpenOption.CREATE;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Class get top 1000 document. Using it for input of k-mean algorithsm
 * 
 * @cteated 5 / 5 / 2015
 * @author baonc
 *
 */
public class Get1000Doc {
	
	/**
	 * function get top 1000 document
	 */
	public void getTop1000() {
		try(InputStream in = Files.newInputStream(Paths.get("/home/baonc/Desktop/top10000"));
				BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			OutputStream out = new BufferedOutputStream(Files.newOutputStream(Paths.get("data/top1000"), CREATE));
			for(int i = 0; i < 1000; i++) {
				String line = reader.readLine();
				out.write(line.getBytes());
				System.out.println("ReadLine: " + (i + 1));
			}
			out.close();
			reader.close();
			in.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
