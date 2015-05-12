package org.study.tfidf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
 * Class using k-mean algorithm to clustering document
 * 
 * @created 		30 / 3 / 2015
 * @versionHadoop 	2.5
 * @author 			baonc
 *
 */
public class KMeanAlg extends Configured implements Tool {
	private static String INPUT_PATH = "Dimension/top10000";
	private static String OUTPUT_PATH = "K-mean";
	
	public static class KMeanAlgMapper extends Mapper<LongWritable, Text,Text, Text> {
		private String k_center[];
		private Text center = new Text();
		private Text document = new Text();
		private int iteration;
		
		@Override
		public void setup(Context context) throws IOException, InterruptedException {
			Configuration conf = context.getConfiguration();
			iteration = Integer.parseInt(conf.get("iteration"));
			k_center = readKCenter(20);
		}
		
		private String[] readKCenter(int k) throws IOException {
			String k_center[] = new String[k];
			Path pt;
			String line = null;
			String center[];
			
			if(iteration == -1) {
				pt = new Path("Dimension/top10000");
			} else {
				pt = new Path(KMeanAlg.OUTPUT_PATH + (iteration - 1) + "/part-r-00000");	// den luc doc thi iteration - 1
			}
			FileSystem fs = FileSystem.get(new Configuration());
			BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(pt)));
			
			for(int i = 0; i < k; i++) {
				line = br.readLine();
				center = line.split("\t");
				k_center[i] = center[0].trim() + "\t" + center[1].trim();
			}
			
			return k_center;
		}
		
		/**
		 * MAP 
		 * 	INPUT: Dimension/part-r-00000
		 * 		Key: offset of line in document
		 * 		Value: content of line in document
		 * 	OUTPUT:	
		 * 		Key: center
		 * 		Value: documents with same center
		 */
		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String documentAndTfidfBuilder;
			documentAndTfidfBuilder = value.toString().split("\t")[1];
			String wordAndTfidfArray[] = documentAndTfidfBuilder.trim().split(" ");
			double minDistance = Double.MAX_VALUE;
			String center = null;
			String wordAndTfidfArrayCenter[];
			String documentAndTfidfCenterBuilder;
			
			for(int i = 0;i < k_center.length; i++) {
				documentAndTfidfCenterBuilder = k_center[i].split("\t")[1];
				wordAndTfidfArrayCenter = documentAndTfidfCenterBuilder.trim().split(" ");
				double distance = Distance.distance(wordAndTfidfArrayCenter, wordAndTfidfArray);
				if(minDistance > distance) {
					minDistance = distance;
					center = k_center[i];
				}
			}
			
			this.center.set(center);
			this.document = value;
			context.write(this.center, this.document);
		}
	}
	
	public static class KMeanAlgReducer extends Reducer<Text, Text, Text, Text> {
		private Text center = new Text();
		private Text document = new Text();
	
		// counter in hadoop
		public enum UpdateCounter {
			UPDATE
		}

		/**
		 * Reduce
		 * 	INTPUT: MAP
		 * 	OUTPUT:	
		 * 		Key: center
		 * 		Value: document
		 */
		@Override
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			String newCenter[] = null;
			String documentAndTfidfOldCenterBuilder;
			documentAndTfidfOldCenterBuilder = key.toString().trim().split("\t")[1];
			String wordAndTfidfArrayOldCenter[] = documentAndTfidfOldCenterBuilder.toString().trim().split(" ");
			String document = "";
			String sumDoc[] = null;
			
			for(Text value : values) {
				document += value.toString().split("\t")[0].trim() + "\t";
				sumDoc = Distance.sum(sumDoc, value.toString().split("\t")[1].trim().split(" "));
			}
			
			// avg document
			newCenter = Distance.avgDocument(sumDoc);	
			
			// convert center array to center string
			String newCenterString = "Center	";
			for(int i = 0; i < newCenter.length; i++) {
				newCenterString += newCenter[i] + " ";
			}
			
			// write newCenter
			this.center.set(newCenterString);
			// write docment with same center
			this.document.set(document);
			context.write(this.center, this.document);

			// Check if center not ... then loop map-reduce
			if(!Distance.isEqual(wordAndTfidfArrayOldCenter, newCenter)) {
				context.getCounter(UpdateCounter.UPDATE).increment(1);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public int run(String[] arg0) throws Exception {
		int iteration = -1;
		Configuration conf = getConf();
		conf.set("iteration", String.valueOf(iteration));
		Job job = new Job(conf, "Kmean-algs");
		
		iteration++;
		FileSystem fs = FileSystem.get(conf);
		if(fs.exists(new Path(KMeanAlg.OUTPUT_PATH + iteration))) {
			fs.delete(new Path(KMeanAlg.OUTPUT_PATH + iteration), true);
		}
		
		job.setJarByClass(KMeanAlg.class);
		job.setMapperClass(KMeanAlg.KMeanAlgMapper.class);
		job.setReducerClass(KMeanAlg.KMeanAlgReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		TextInputFormat.addInputPath(job, new Path(KMeanAlg.INPUT_PATH));
		TextOutputFormat.setOutputPath(job, new Path(KMeanAlg.OUTPUT_PATH + iteration));
		
		job.waitForCompletion(true);
		
		long counter = job.getCounters().findCounter(KMeanAlgReducer.UpdateCounter.UPDATE).getValue();
		iteration++;
		while(counter > 0) {
			conf = new Configuration();
			conf.set("iteration", String.valueOf(iteration));
			conf.set("num.iteration", iteration + "");
			job = new Job(conf, "Kmean-Alg : Iteration = " + iteration);
			
			job.setJarByClass(KMeanAlg.class);
			job.setMapperClass(KMeanAlg.KMeanAlgMapper.class);
			job.setReducerClass(KMeanAlg.KMeanAlgReducer.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(Text.class);
			job.setInputFormatClass(TextInputFormat.class);
			job.setOutputFormatClass(TextOutputFormat.class);
			TextInputFormat.addInputPath(job, new Path(KMeanAlg.INPUT_PATH));
			TextOutputFormat.setOutputPath(job, new Path(KMeanAlg.OUTPUT_PATH + iteration));
			
			job.waitForCompletion(true);
			iteration++;
			counter = job.getCounters().findCounter(KMeanAlg.KMeanAlgReducer.UpdateCounter.UPDATE).getValue();
		}
		return 0;
	}
}