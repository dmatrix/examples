from __future__ import print_function

import sys

from pyspark.sql import SparkSession
from pyspark.sql.functions import col,count


if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: mnmcount <file>", file=sys.stderr)
        sys.exit(-1)

    spark = SparkSession\
        .builder\
        .appName("PythonMnMCount")\
        .getOrCreate()
    mnm_file = sys.argv[1]
    mnm_df = spark.read.format("csv") \
        .option("header", "true") \
        .option("inferSchema", "true") \
        .load(mnm_file)
    # aggregate count of all colors and groupBy state and color
    # orderBy descending order
    count_mnm_df = mnm_df.select("State", "Color", "Count") \
                    .groupBy("State", "Color") \
                    .agg(count("Count") \
                    .alias("Total")) \
                    .orderBy("Total", ascending=False)

    count_mnm_df.show(n=60, truncate=False)
    print("Total Rows = %d" % (count_mnm_df.count()))
    #
    # find the aggregate count for California
    ca_count_mnm_df = mnm_df.select("*") \
                       .where(mnm_df.State == 'CA') \
                       .groupBy("State", "Color") \
                       .agg(count("Count") \
                            .alias("Total")) \
                       .orderBy("Total", ascending=False)
    ca_count_mnm_df.show(n=10, truncate=False)
    # stop the SparkSession
    spark.stop()

