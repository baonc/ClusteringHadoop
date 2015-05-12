package org.study.vnTokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.*;

import vn.hus.nlp.tokenizer.VietTokenizer;

/**
 * Lop tach tu cho van ban tieng viet 
 *  dau vao la file vua duoc sinh ra tu lop GeneratingFile.java
 * 
 * @created 20 / 02 / 2015
 * @author baonc
 *
 */
public class VNTokenizer {
	private String inputFile = "data/CRAWLE_NEWS_TOP_1.txt";
	private String tempOutputFile = "data/CRAWLE_NEWS_TOP_1_VIET_TOKEN";
	private String outputFile = "data/CRAWLE_NEWS_TOP_100K_VIET_TOKEN.txt";
	
	public void vnToken() {
		String line = null;
		String enter = "\n";
		VietTokenizer tokenizer;
		
		System.out.println("Start tokenizer...");
		/**
		 * Do file qua lon nen doc tung dong cua file CRAWLE_NEWS_TOP_100K.txt
		 * Ghi ra file CRAW_NEWS_TOP_1.txt roi tach tu tren vb do.
		 * Sau khi tach tu xong thi ghi lai vao file CRAWLE_NEWS_TOP_100K_VIET_TOKEN.txt
		 */
		try(InputStream in = Files.newInputStream(Paths.get("data/CRAWLE_NEWS_TOP_100K.txt"));
				BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			//OutputStream writer = Files.newOutputStream(Paths.get(this.outputFile));
			OutputStream writer = Files.newOutputStream(Paths.get(this.outputFile), APPEND);
			tokenizer = new VietTokenizer();
			
			for(int i = 0; i < 100000; i++) {
				// doc tung dong cua vb CRAWLE_NEWS_TOP_100k.txt
				line = reader.readLine();
				System.out.println("Processing: " + i + "%");
				// bo qua nhung dong da xu ly
				if(i <= 96683) {
					continue;
				}
				// ghi lai dong vua doc vao vb CRAW_NEWS_TOP_1.txt
				byte data[] = line.getBytes();
				OutputStream out = Files.newOutputStream(Paths.get(this.inputFile));
				out.write(data);
				out.close();
				// tach tu tren vb CRAW_NEWS_TOP_1.txt
				this.tempOutputFile = new String("data/CRAWLE_NEWS_TOP_1_VIET_TOKEN");
				this.inputFile = new String("data/CRAWLE_NEWS_TOP_1.txt");
				// khong hieu tai sao dong code nay lai khong chay khi i = 52.
				//tokenizer.tokenize(this.inputFile, this.tempOutputFile);
//				if(i == 52 || i == 144 || i == 265 || i == 271 || i == 491) {
//					//tokenizer = null;
//					//System.gc();
//					//tokenizer = new VietTokenizer();
//					//tokenizer.tokenize(this.inputFile, this.outputFile);
//					continue;
//				} else {
//					tokenizer.tokenize(this.inputFile, this.tempOutputFile);
//				}
				if(i == 1692 || i == 2522 || i == 12524 || i == 13812 || i == 15300 || i == 15988 || i == 16045 || i == 18957
						|| i == 22748 || i == 22843 || i == 36637 || i == 49843 || i == 50742 || i == 59217 || i == 65070
						|| i == 73350 || i == 75825 || i == 86868 || i == 88657 || i == 92630 || i == 96683) {
					continue;
				}
				try {
					tokenizer.tokenize(this.inputFile, this.tempOutputFile);
				} catch(Exception e) {
					continue;
				}
				// doc tu file CRAWLE_NEW_TOP_1_VIET_TOKEN.txt roi ghi vao file CRAWLE_NEWS_TOP_100K_VIET_TOKEN.txt
				InputStream inputStream = Files.newInputStream(Paths.get(this.tempOutputFile));
				BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
				line = bufferReader.readLine();
				data = line.getBytes();
				writer.write(data);
				data = enter.getBytes();
				writer.write(data);
				bufferReader.close();
				inputStream.close();
			}
			
			reader.close();
			in.close();
			writer.close();
		} catch(IOException ioe) {
			System.err.println(ioe);
		}
		System.out.println("End tokenizer.");
	}
}