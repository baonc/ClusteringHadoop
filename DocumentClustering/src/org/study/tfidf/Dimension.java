package org.study.tfidf;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;

/**
 * Lop chuyen tu dang tu@VB	TFIDF sang dang VB	tu=tfidf
 * 
 * @Created 28 / 3 / 2015
 * @hadoopVersion 2.5
 * @author baonc
 *
 */
public class Dimension extends Configured implements Tool {
	private static final String INPUT_PATH = "TFIDF/part-r-00000";
	private static final String OUTPUT_PATH = "Dimension";
	
	public static class DimensionMapper extends Mapper<LongWritable, Text, Text, Text> {
		private Text document = new Text();
		private Text wordAndTfidf = new Text();
		
		/**
		 * Map
		 * 	INPUT:	TFIDF/part-r-00000
		 * 		Key: offset cua dong VB
		 * 		Value: noi dung cua dong VB
		 * 	OUTPUT:
		 * 		Key: VB
		 * 		Value: tu=tfidf
		 */
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String wordDocumentAndTfidf[] = value.toString().split("\t");
			String wordAndDocument[] = wordDocumentAndTfidf[0].split("@");
			String document = wordAndDocument[1];
			this.document.set(document);
			String tfidfWeight[] = wordDocumentAndTfidf[1].split(", ");
			String wordAndTfidf = wordAndDocument[0] + "=" + tfidfWeight[2].replace(']', '0');
			this.wordAndTfidf.set(wordAndTfidf);
			
			context.write(this.document, this.wordAndTfidf);
		}
	}
	
	public static class DimensionReducer extends Reducer<Text, Text, Text, Text> {
		private Text document = new Text();
		private Text wordAndTfidf = new Text();

		/**
		 *	Reduce
		 *		INPUT: maper 
		 *		OUTPUT:	key: document
		 *				value: tu=tfidf tu=tfidf...
		 */
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			String wordAndTfidf = "";

			for(Text value : values) {
				wordAndTfidf += value.toString() + " ";
			}
			
			this.document = key;
			this.wordAndTfidf.set(wordAndTfidf);
			context.write(this.document, this.wordAndTfidf);
		}
	}
	
	@SuppressWarnings({ "deprecation", "static-access" })
	@Override
	public int run(String[] arg0) throws Exception {
		Configuration conf = getConf();
		Job job = new Job(conf, "Dimension");
		
		FileSystem fs = FileSystem.get(conf);
		if(fs.exists(new Path(this.OUTPUT_PATH))) {
			fs.delete(new Path(this.OUTPUT_PATH), true);
		}
		
		job.setJarByClass(Dimension.class);
		job.setMapperClass(Dimension.DimensionMapper.class);
		job.setReducerClass(Dimension.DimensionReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		TextInputFormat.addInputPath(job, new Path(this.INPUT_PATH));
		TextOutputFormat.setOutputPath(job, new Path(this.OUTPUT_PATH));
		
		return job.waitForCompletion(true) ? 0 : 1;
	}

}
