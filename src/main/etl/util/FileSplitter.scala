package etl.util

import org.apache.hadoop.io.compress.GzipCodec
import org.apache.spark.SparkContext

object FileSplitter {
  def splitFile(sc: SparkContext, inputFile: String, outputUri: String): Unit = {
    val compressedFile = sc.textFile(inputFile).repartition(16)
    compressedFile.saveAsTextFile(
      outputUri,
      classOf[GzipCodec]
    )
  }
}