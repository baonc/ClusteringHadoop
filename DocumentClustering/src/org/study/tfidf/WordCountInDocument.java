package org.study.tfidf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Lop de tinh tan so xuat hien cua mot tu trong mot vb
 * 
 * @versionHadoop 2.5
 * @created 4 / 3 / 2015
 * @author baonc
 *
 */
public class WordCountInDocument extends Configured implements Tool {
	// noi dat Output
	private static final String OUTPUT_PATH = "2-word-counts";
	// noi dat Input
	private static final String INPUT_PATH = "1-word-freq";
	
	public static class WordCountInDocMapper extends Mapper<LongWritable, Text, Text, Text> {
		private Text docName = new Text();
		private Text wordAndCount = new Text();
		
		/**
		 * Buoc map
		 * 	INPUT:
		 * 		- Key: by offset cua dong trong VB
		 * 		- Value: Noi dung cua dong trong VB
		 * 	OUTPUT:
		 * 		- Key: Ten VB
		 * 		- Value: So lan xuat hien cua tu trong VB
		 * 	VD:
		 * 		<offet, ssfsf> --> <"1", "sfs=98">
		 */
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String wordAndDocCounter[] = value.toString().split("\t");
			String wordAndDoc[] = wordAndDocCounter[0].split("@");
			this.docName.set(wordAndDoc[1]);
			this.wordAndCount.set(wordAndDoc[0] + "=" + wordAndDocCounter[1]);
			context.write(this.docName, this.wordAndCount);
		}
	}
	
	public static class WordCountInDocReducer extends Reducer<Text, Text, Text, Text> {
		private Text wordAtDoc = new Text();
		private Text wordAvar = new Text();
		
		/**
		 * Buoc reduce
		 * 	INPUT: 
		 * 		- Key: Ten VB
		 * 		- Value: List<tu = so lan xuat hien trong VB>
		 * 	OUTPUT:
		 * 		- Key: Tu@TenVB
		 * 		- Value: Tan so xuat hien trong VB
		 * 		- VD: <Tu@TenVB, 5/13>
		 */
		protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			int sumOfWordInDocument = 0;
			Map<String, Integer> tempCounter = new HashMap<String, Integer>();
			
			for(Text value : values) {
				String wordCounter[] = value.toString().split("=");
				tempCounter.put(wordCounter[0], Integer.valueOf(wordCounter[1]));
				sumOfWordInDocument += Integer.parseInt(value.toString().split("=")[1]);
			}
			
			for(String wordKey : tempCounter.keySet()) {
				this.wordAtDoc.set(wordKey + "@" + key.toString());
				this.wordAvar.set(tempCounter.get(wordKey) + "/" + sumOfWordInDocument);
				context.write(this.wordAtDoc, this.wordAvar);
			}
		}
	}
	
	@SuppressWarnings("static-access")
	@Override
	public int run(String[] arg0) throws Exception {
		Configuration conf = getConf();
		@SuppressWarnings("deprecation")
		Job job = new Job(conf, "Word Count In Document");
		
		job.setJarByClass(WordCountInDocument.class);
		job.setMapperClass(WordCountInDocMapper.class);
		job.setReducerClass(WordCountInDocReducer.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
			
		FileInputFormat.addInputPath(job, new Path(this.INPUT_PATH));
		FileOutputFormat.setOutputPath(job, new Path(this.OUTPUT_PATH));
		
		return job.waitForCompletion(true) ? 0 : 1;
	}
	
	public static void main(String args[]) throws Exception {
		int res = ToolRunner.run(new Configuration(), new WordCountInDocument(), args);
		System.exit(res);
	}
}
