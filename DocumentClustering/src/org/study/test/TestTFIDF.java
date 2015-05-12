package org.study.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestTFIDF {
	private Set<Integer> document = new HashSet<Integer>();
	
	public void checkNumberOfDocument() {
		String line = null;
		String wordDocumentAndTFIDF[];
		String wordAndDocument[];
		
		try(InputStream in = Files.newInputStream(Paths.get("data/part-r-00000"));
				BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			while((line = reader.readLine()) != null) {
				wordDocumentAndTFIDF = line.split("\t");
				wordAndDocument = wordDocumentAndTFIDF[0].split("@");
				this.document.add(Integer.parseInt(wordAndDocument[1]));
			}
		} catch(IOException ioe) {
			System.err.println(ioe);
		}
	}
	
	public void print() {
		for(Integer doc : this.document) {
			System.out.println(doc);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void sort() {
		@SuppressWarnings({ "rawtypes" })
		List sortedList = new ArrayList(this.document);
		Collections.sort(sortedList);
		
		for(int i = 0; i < sortedList.size(); i++) {
			System.out.println(sortedList.get(i));
		}
	}
	
	public static void main(String args[]) {
		TestTFIDF test = new TestTFIDF();
		test.checkNumberOfDocument();
		test.print();
		//test.sort();
	}
}