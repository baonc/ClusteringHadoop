package org.study.tfidf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * Lop tach tu tu file STOPWORDLIST.txt roi them cac stop word vao mot danh sach stop word
 * 
 * @author baonc
 *
 */
public class StopWord {
	private Set<String> stopWords;
	
	public StopWord() throws IOException {
		//this.stopWordToken();
		this.addStopWord();
	}
	
	public Set<String> getStopWord() {
		return this.stopWords;
	}
	
	/**
	 * tach tu tieng viet trong tep stop word
	 */
//	public void stopWordToken() {
//		VietTokenizer tokenizer = new VietTokenizer();
//		tokenizer.tokenize(this.inputFile, this.outputFile);
//	}
	
	/**
	 * Doc cac tu trong tep stop word (STOPWORDLIST_VIET_TOKEN.txt)
	 * roi them chung vao Set<String> stopWord
	 * @throws IOException 
	 */
	public void addStopWord() {
		String line = null;
		this.stopWords = new HashSet<String>();
		Path pt = new Path("hdfs:/Stopword/STOPWORDLIST_VIET_TOKEN.txt");
		FileSystem fs = null;
		try {
			fs = FileSystem.get(new Configuration());
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(fs.open(pt)));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			while((line = br.readLine()) != null) {
				this.stopWords.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}