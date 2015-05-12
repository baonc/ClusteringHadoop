package org.study.tfidf;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
//import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.util.Tool;

/**
 * Lop tinh gia tri tf-idf cho mot tu
 * 
 * @versionHadoop 2.5
 * @created 4 / 3 / 2015
 * @author baonc
 *
 */
public class WordsInCorpusTFIDF extends Configured implements Tool {
	// Noi dat output
	private static final String OUTPUT_PATH = "1-word-freq";
	private static final String OUTPUT_PATH_2 = "2-word-counts";
	
	public static class WordInCorpusTFIDFMapper extends Mapper<LongWritable, Text, Text, Text> {
		private Text WordAndDoc = new Text();
		private Text WordAndDocCounter = new Text();
		
		/**
		 * Buoc Map
		 * 	INPUT:
		 * 		- Key: offset cua mot dong trong VB
		 * 		- Value: Noi dung cua dong trong VB
		 *	OUTPUT:
		 *		- PRE: marcelo@book	3/1500
		 *		- POS: marcelo, book=3/1500,1
		 */
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String wordAndCounters[] = value.toString().split("\t");
			String wordAndDoc[] = wordAndCounters[0].split("@");
			this.WordAndDoc.set(new Text(wordAndDoc[0]));
			this.WordAndDocCounter.set(wordAndDoc[1] + "=" + wordAndCounters[1]);
			context.write(this.WordAndDoc, this.WordAndDocCounter);
		}
	} 
	
	public static class WordInCorpusTFIDFReducer extends Reducer<Text, Text, Text, Text> {
		private static final DecimalFormat DF = new DecimalFormat("###.########");
		private Text wordAtDocument = new Text();
		private Text tfidfCounts = new Text();
		
		/**
		 * Buoc reduce
		 * 	INPUT:
		 * 		- Key: tu
		 * 		- Value: VB1=n1/N1, VB2=n2/N2
		 * 	OUPUT:
		 * 		- Key: tu@VB
		 * 		- Value: d/D, f/total, tfidf
		 */
		protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, 
		InterruptedException {
			// lay so luong VB tu file system
			//int numberOfDocumentsInCorpus = context.getConfiguration().getInt("numberOfDocsInCorpus", 0);
			int numberOfDocumentsInCorpus = 99221;
			// tong so luong tan so cua mot tu.
			int numberOfDocumentsInCorpusWhereKeyAppear = 0;
			Map<String, String> tempFrequencies = new HashMap<String, String>();
			
			for(Text value : values) {
				String documentAndFrequencies[] = value.toString().split("=");
				
				// neu so luong tu > 0
				if(Integer.parseInt(documentAndFrequencies[1].split("/")[0]) > 0) {
					numberOfDocumentsInCorpusWhereKeyAppear++;
				}
				tempFrequencies.put(documentAndFrequencies[0], documentAndFrequencies[1]);
			}
			
			for(String document : tempFrequencies.keySet()) {
				String wordFrequenceAndTotalWords[] = tempFrequencies.get(document).split("/");
				double tf = Double.valueOf(Double.valueOf(wordFrequenceAndTotalWords[0]) 
						/ Double.valueOf(wordFrequenceAndTotalWords[1]));
				double idf = Math.log10((double) numberOfDocumentsInCorpus / 
						(double) ((numberOfDocumentsInCorpusWhereKeyAppear == 0 ? 1 : 0) 
								+ numberOfDocumentsInCorpusWhereKeyAppear));
				double tf_idf = tf * idf;
				
				this.wordAtDocument.set(key + "@" + document);
				this.tfidfCounts.set("[" + numberOfDocumentsInCorpusWhereKeyAppear + "/" + numberOfDocumentsInCorpus
						+ ", " + wordFrequenceAndTotalWords[0] + "/" + wordFrequenceAndTotalWords[1] + ", " 
						+ DF.format(tf_idf) + "]");
				
				context.write(this.wordAtDocument, this.tfidfCounts);
			}
		}
	}
	
	@SuppressWarnings("static-access")
	@Override
	public int run(String[] args) throws Exception {
		Configuration conf = getConf();
		FileSystem fs = FileSystem.get(conf);
		
		if(args[0] == null || args[1] == null) {
			System.out.println("");
		}
		
		Path userInputPath = new Path(args[0]);
		
		Path userOutputPath = new Path(args[1]);
		if(fs.exists(userOutputPath)) {
			fs.delete(userOutputPath, true);
		}
		
		Path wordFreqPath = new Path(this.OUTPUT_PATH);
		if(fs.exists(wordFreqPath)) {
			fs.delete(wordFreqPath, true);
		}
		
		Path wordCountPath = new Path(this.OUTPUT_PATH_2);
		if(fs.exists(wordCountPath)) {
			fs.delete(wordCountPath, true);
		}
		
		// lay so luong document
//		FileStatus userFilesStatusList[] = fs.listStatus(userInputPath);
//		final int numberOfUserInputFiles = userFilesStatusList.length;
//		String fileNames[] = new String[numberOfUserInputFiles];
//		for(int i = 0; i < numberOfUserInputFiles; i++) {
//			fileNames[i] = userFilesStatusList[i].getPath().getName();
//		}
		
		@SuppressWarnings("deprecation")
		Job job = new Job(conf, "Word Frequence In Documents");
		job.setJarByClass(WordFrequenceInDocument.class);
		job.setMapperClass(WordFrequenceInDocument.WordFrequenceInDocMapper.class);
		job.setReducerClass(WordFrequenceInDocument.WordFrequenceInDocReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		TextInputFormat.addInputPath(job, userInputPath);
		TextOutputFormat.setOutputPath(job, new Path(this.OUTPUT_PATH));
		
		job.waitForCompletion(true);
		
		Configuration conf2 = getConf();
		//conf2.setStrings("documentsInCorpusList", fileNames);
		@SuppressWarnings("deprecation")
		Job job2 = new Job(conf2, "Words Count");
		job2.setJarByClass(WordCountInDocument.class);
		job2.setMapperClass(WordCountInDocument.WordCountInDocMapper.class);
		job2.setReducerClass(WordCountInDocument.WordCountInDocReducer.class);
		job2.setOutputKeyClass(Text.class);
		job2.setOutputValueClass(Text.class);
		job2.setInputFormatClass(TextInputFormat.class);
		job2.setOutputFormatClass(TextOutputFormat.class);
		TextInputFormat.addInputPath(job2, new Path(this.OUTPUT_PATH));
		TextOutputFormat.setOutputPath(job2, new Path(this.OUTPUT_PATH_2));
		
		job2.waitForCompletion(true);
		
		Configuration conf3 = getConf();
		//conf3.setInt("numberOfDocsInCorpus", numberOfUserInputFiles);
		@SuppressWarnings("deprecation")
		Job job3 = new Job(conf3, "TF-IDF of word in corpus");
		job3.setJarByClass(WordsInCorpusTFIDF.class);
		job3.setMapperClass(WordsInCorpusTFIDF.WordInCorpusTFIDFMapper.class);
		job3.setReducerClass(WordsInCorpusTFIDF.WordInCorpusTFIDFReducer.class);
		job3.setOutputKeyClass(Text.class);
		job3.setOutputValueClass(Text.class);
		job3.setInputFormatClass(TextInputFormat.class);
		job3.setOutputFormatClass(TextOutputFormat.class);
		TextInputFormat.addInputPath(job3, new Path(this.OUTPUT_PATH_2));
		TextOutputFormat.setOutputPath(job3, userOutputPath);
		
		return job3.waitForCompletion(true) ? 0 : 1;
	}

}
