# Freckle Data Engineer Challenge
An open data engineering challenge based on real location data. Each entry in this data set is a "location-event". The idfa is the unique identifier of the user.

The expectation for this exercise is that you use Spark 2.x with Scala, Python, or Java. You can use the RDD or Dataframe APIs as you see fit, but please be ready to explain your choices. You must do your work over the entire dataset.

**Instructions:**

1. Fork this repo with your own id for our review.
2. Download the dataset here: https://s3.amazonaws.com/freckle-dataeng-challenge/location-data-sample.tar.gz
3. Answer: What is the max, min, avg, std dev for the location-events per IDFA?
4. Produce geohashes for all coordinates in a new RDD or DataFrame
5. Using the geohashes, determine if there clusters of people at any point in this dataset. If so, how many people and how close are they?
6. Write any findings into a local parquet format file for later use. 
7. *Bonus*: Conduct any additional analysis that might give a hint about the behaviour of the IDFAs in the data set.

Please complete as much of the assignment as you have time for. How long you had time to spend on the challenge and your experience will be considered. Have some fun with it!

**Running the program:**

1. Download the location-data-sample.tar.gz to local disk and extract it. Don't have to extract the individual gz files inside location-data-sample.tar.gz. Spark can gunzip them.

2. mvn clean compile exec:java -Din="path to input directory" -Dout="path to output directory"

*For Example*
```
$ wget https://s3.amazonaws.com/freckle-dataeng-challenge/location-data-sample.tar.gz
$ tar -xzvf /home/riyaz/dechallenge/location-data-sample.tar.gz
$ mvn clean compile exec:java -Din=/home/riyaz/dechallenge/location-data-sample -Dout=/home/riyaz/dechallenge/output
```

**IDFA Stats:**
```
+-----+---+-----+------+                                                        
|  Max|Min|  Avg|StdDev|
+-----+---+-----+------+
|15979|  1|36.75|118.61|
+-----+---+-----+------+
```

**People Clusters:**

*Visualisation*

People clusters with population > 10 can be visualised on Google Maps using the following link
http://htmlpreview.github.io/?https://github.com/hmrizin/DataEngChallenge/blob/master/map-visualisation/clusters.html

*Top Clusters*
```
+------------+-----+
|     geohash|count|
+------------+-----+
|s00000000000| 4427| Looks like dummy/test events or events generated with no access to geo location
|djfq0rzn7m70|   75|
|dq21mmek4q6q|   73|
|9vkh7wddguw5|   63|
|dpz8336uu2eq|   61|
|f244mdxpncbp|   58|
|dpm5wpyg42f9|   52|
|djfmbs7xs1j8|   48|
|f241b833vv6j|   47|
|djgzq3q23u2p|   46|
|djkvw9r4j8vp|   45|
|dn6m9tgey6mq|   44|
|djt54wb39fhy|   44|
|djdxvzvm9wvu|   43|
|dnkkg7cw8k1b|   39|
|dpscv16bk3zf|   38|
|dnq1zws4u9te|   38|
|dpherfur8ezf|   38|
|f2418x4h86s2|   37|
|c2b2mbftz52c|   37|
|c28rvbv6s3tc|   37|
|dqcx8295qpmv|   36|
|dnhmq5dxr3p4|   36|
|djg94g4mr43y|   36|
|f244my2bcycv|   36|
+------------+-----+
```
**Additional Analysis:**

IDFA's with the most unique checkins. That is, people who move around a lot
```
+--------------------+----+                                                     
|                idfa|  ct|
+--------------------+----+
|00000000-0000-000...|7882|
|a3a56a19-380d-440...| 729|
|72afbdae-da50-48f...| 633|
|400b6114-d23f-431...| 514|
|7c4b9080-f0b0-4a3...| 479|
|05f27859-cbb2-4be...| 477|
|6c9aa53a-71fa-4d9...| 455|
|e22a0a1c-34cb-491...| 445|
|d9724696-0a92-460...| 443|
|a044c5cc-1f07-4cb...| 434|
|38fe4b97-a1a2-459...| 425|
|eb142ffd-ac37-43c...| 392|
|f82459e1-8cc4-45a...| 382|
|94973cb2-d8a1-465...| 380|
|3ffcc261-1fca-44f...| 367|
|f08d0f1a-56c3-480...| 365|
|51754256-8fd4-428...| 364|
|37f6234c-774e-440...| 347|
|da0e90fb-19e0-487...| 343|
|0b16f143-c8b2-40c...| 332|
+--------------------+----+
```

IP's with the most checkins. A very high number indicates shared ip like wifi-hotspot or vpn
```
+--------------+---+                                                            
|       user_ip| ct|
+--------------+---+
| 107.77.160.22| 84|
| 107.77.160.30| 77|
| 107.77.160.51| 77|
| 107.77.160.38| 76|
| 107.77.160.27| 76|
| 107.77.160.54| 76|
| 107.77.160.32| 71|
| 107.77.160.29| 70|
| 107.77.160.25| 70|
| 107.77.160.31| 69|
| 107.77.160.24| 69|
| 107.77.160.21| 69|
| 107.77.160.90| 68|
| 107.77.160.19| 66|
| 107.77.160.78| 66|
| 107.77.160.37| 66|
|107.77.160.104| 66|
| 173.46.76.190| 65|
| 107.77.160.52| 64|
| 173.46.76.142| 64|
+--------------+---+
```
