package org.study.tfidf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;

/**
 * Lop su dung map-reduce de tinh so lan xuat hien cua mot tu trong 
 * mot document
 * 
 * @versionHadoop 2.5
 * @created 27 / 02 / 2015
 * @author baonc
 *
 */
public class WordFrequenceInDocument extends Configured implements Tool {
	// noi dat output
	private static final String OUTPUT_PATH = "1-word-freq";

	public static class WordFrequenceInDocMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
		private static final Pattern PATTERN = Pattern.compile("\\w+", Pattern.UNICODE_CHARACTER_CLASS);
		private Text word = new Text();
		private IntWritable singleCount = new IntWritable(1);
		private static Set<String> stopWord;
		
		static {
			stopWord = new HashSet<String>();
			try {
				stopWord = readStopWord();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
		
		/**
		 * Ham doc stopword luu trong hdfs:/Stopword/STOPWORDLIST_VIET_TOKEN.txt
		 */
		public static Set<String> readStopWord() throws IOException {
			Path pt = new Path("Stopword/STOPWORDLIST_VIET_TOKEN.txt");
			FileSystem fs = FileSystem.get(new Configuration());
			BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(pt)));
			String line = null;
			Set<String> stopWord = new HashSet<String>();
			
			while((line = br.readLine()) != null) {
				stopWord.add(line);
			}
			
			return stopWord;
		}
		
		/**
		 * Buoc map
		 * INPUT:
		 * 	key: offset cua mot dong tu dau van ban.
		 * 	value: noi dung cua mot dong van ban.
		 * OUTPUT:
		 * 	key: mot tu trong van ban@0ffset dong trong van ban
		 * 	value: so lan xuat hien toi thieu trong van ban. la 1.
		 * 
		 * UPDATE LAN 3:
		 * 	Chinh ve dang offset file chu khoong phai so dong cua VB.
		 * 
		 */
		@SuppressWarnings("static-access")
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			// su dung regex de tach cac tu trong van ban.
			Matcher matcher = PATTERN.matcher(value.toString());
			// ghi lai gia tri OUTPUT::value
			StringBuilder valueBuilder = new StringBuilder();

			while(matcher.find()) {
				String matcherKey = matcher.group().toLowerCase();
				/**
				 * Loai bo cac tu vo nghia
				 * 	- Bat dau bang mot ky tu khong phai la mot chu cai
				 * 	- La cac chu so
				 * 	- La cac stop word
				 *
				 */
				if(!Character.isLetter(matcherKey.charAt(0)) || Character.isDigit(matcherKey.charAt(0)) 
						|| this.stopWord.contains(matcherKey) || matcherKey.length() < 3) {
					continue;
				}
				valueBuilder.append(matcherKey);
				valueBuilder.append("@");
				valueBuilder.append(key.toString());
				// OUTPUT::<value:key>
				this.word.set(valueBuilder.toString());
				context.write(this.word, this.singleCount);
				valueBuilder.setLength(0);
			}
		//	context.write(key, this.singleCount);
//			String words[] = value.toString().split(" ");
//			StringBuilder valueBuilder = new StringBuilder();
//			
//			for(int i = 0; i < words.length; i++) {
//				String word = words[i].toLowerCase().trim();
//				
//				if(!Character.isLetter(word.charAt(0)) || Character.isDigit(word.charAt(0)) || 
//						this.stopWord.getStopWord().contains(word) || word.length() < 3) {
//					continue;
//				}
//				valueBuilder.append(word);
//				valueBuilder.append("@");
//				valueBuilder.append(key.toString());
//				this.word.set(valueBuilder.toString());
//				context.write(this.word, this.singleCount);
//				valueBuilder.setLength(0);
//			}
		}
	}
	
	public static class WordFrequenceInDocReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
		// so lan xuat hien cua mot tu trong van ban
		private IntWritable wordSum = new IntWritable();
		
		/**
		 * Buoc Reduce
		 * 	INPUT:
		 * 	- Output cua buoc map
		 * 		- key: tu trong van ban
		 * 		- value: list<1>
		 * 	OUTPUT:
		 * 		- key: tu trong van ban
		 * 		- output: so lan xuat hien cua tu trong van ban
		 */
		protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, 
		InterruptedException {
			int sum = 0;
			
			for(IntWritable value : values) {
				sum += value.get();
			}
			// OUTPUT::<key:value>
			this.wordSum.set(sum);
			context.write(key, this.wordSum);
		}
	}
	
	@SuppressWarnings("static-access")
	public int run(String args[]) throws Exception {
		Configuration conf = getConf();
		@SuppressWarnings("deprecation")
		Job job = new Job(conf, "Word frequence in document");
		
		FileSystem fs = FileSystem.get(conf);
		if(fs.exists(new Path(this.OUTPUT_PATH))) {
			fs.delete(new Path(this.OUTPUT_PATH), true);
		}
		
		job.setJarByClass(WordFrequenceInDocument.class);
		job.setMapperClass(WordFrequenceInDocMapper.class);
		job.setReducerClass(WordFrequenceInDocReducer.class);
		
		job.setOutputKeyClass(Text.class);
		//job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(IntWritable.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(this.OUTPUT_PATH));
		
		return job.waitForCompletion(true) ? 0 : 1;
	}
}