package com.freckle;

import static org.apache.spark.sql.functions.*;

import org.apache.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

public class DataEnggChallenge {
	
	static String inputPath = "/home/riyaz/dechallenge/sample";
	static String outputPath = "/home/riyaz/dechallenge/sampleout";
	
	private static final Logger logger = Logger.getLogger(DataEnggChallenge.class);
	
	public static void main(String[] args) {
		if (args.length < 2) {
			// mvn exec:java -Din=/home/riyaz/dechallenge/sample -Dout=/home/riyaz/dechallenge/sampleout
			logger.error("Mandatory arguments missing. Usage: mvn exec:java -Din=inputpath -Dout=outputpath");
			return;
		}
		
		inputPath = args[0];
		outputPath = args[1];
		
		/*
		 * Using local master given the nature of this exercise. In production
		 * code the master address will be parameterised or passed via
		 * spark-submit
		 */
		SparkSession spark = SparkSession
				  .builder()
				  .master("local[*]")
				  .appName("DataEnggChallenge")
				  .getOrCreate();
		
		logger.info("Input Path: " + inputPath);
		logger.info("Output Path: " + outputPath);
		
		Dataset<Row> df = spark.read().json(inputPath);
		
		/*
		 * Compute and store basic stats
		 */
		String idfaStatOutPath = outputPath + "/stats";
		logger.info("Writing idfa stats to: " + idfaStatOutPath);
		String ct = "ct";
		int rnd = 2;
		Dataset<Row> idfaStats = df
				.groupBy("idfa")
				.agg(count("*").as(ct))
				.agg(
					max(ct).as("Max"),
					min(ct).as("Min"), 
					round(avg(ct), rnd).as("Avg"), 
					round(stddev(ct), rnd).as("StdDev")
				);
		idfaStats.write().mode(SaveMode.Overwrite).option("header", "true").csv(idfaStatOutPath);
		
		/*
		 * Find 'clusters of people' using the simplest approach. This works
		 * well for given the 12 point precision of the geohashes in the input
		 * dataset.
		 * 
		 * This approach does have the following limitations:
		 * 
		 * 1) A geohash cannot have more than once cluster. Hence a this
		 * approach cannot be used with geohashes with lower precision
		 * 
		 * 2) A cluster cannot cross geohash boundaries
		 * 
		 * Alternative approaches to consider
		 * 
		 * a) Make use of store bounding boxes (if available) to find overlap
		 * between stores and geohashes
		 * 
		 * b) Based on some quick reading on the web it may be possible to use
		 * clustering algorithms like k-means or dbscan on the lat/lon directly.
		 * This approach needs further exploration
		 */
		String clusterOutPath = outputPath + "/clusters";
		logger.info("Writing people cluseters in parquet format to : " + clusterOutPath);
		String count = "count";
		Dataset<Row> cluster = df
				.groupBy(col("geohash"))
				.agg(countDistinct("idfa").as(count))
				.filter(col(count).gt(1));
		cluster.write().mode(SaveMode.Overwrite).parquet(clusterOutPath);

		logger.info("Verifying people clusters parquet file. Read top 25 clusters");
		spark.read().parquet(clusterOutPath).sort(col("count").desc()).show(25);
		
		spark.stop();
	}
}
