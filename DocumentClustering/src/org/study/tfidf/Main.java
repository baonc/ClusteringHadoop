package org.study.tfidf;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;

/**
 * Lop main
 * 
 * @hadoopVersion 2.5
 * @author baonc
 *
 */
public class Main {
	public static void main(String args[]) throws Exception {
		int res = ToolRunner.run(new Configuration(), new KMeanAlg(), args);
		System.exit(res);
	}
}