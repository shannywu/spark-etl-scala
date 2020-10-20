package etl.extractor

import etl.util.TypeGenerator
import types.TAccessLog
import org.apache.spark.SparkContext
import play.api.libs.json.Json
import org.apache.hadoop.mapreduce.Job
import org.apache.parquet.hadoop.thrift.ParquetThriftOutputFormat
import org.apache.parquet.hadoop.ParquetOutputFormat

class SampleLogExtractor extends ExtractorTrait {

  def extract(sc: SparkContext, job: Job, inputFile: String, outputUri: String): Unit = {
    val numResultPartitions = 8

    val rdd = sc.textFile(inputFile)
      .flatMap { line =>
        try {
          val json = Json.parse(line)

          Some((null, new TAccessLog(
            (json \ "remote_ip").asOpt[String].getOrElse(""),
            (json \ "received_at").asOpt[Long].getOrElse(0L) * 1000,
            (json \ "request").asOpt[String].getOrElse(""),
            (json \ "status").asOpt[Int].getOrElse(0),
            (json \ "size").asOpt[Int].getOrElse(0),
            (json \ "duration").asOpt[Int].getOrElse(0),
            (json \ "referer").asOpt[String].getOrElse(""),
            (json \ "user_agent").asOpt[String].getOrElse(""),
            TypeConverter.convertMap(json \ "post_query_detail")
            TypeGenerator.genTRequestDetail(json \ "request_detail"),
            TypeGenerator.genTIpDetail(json \ "remote_ip_detail"),
          )))
        } catch {
          case e: Exception => {
            None
            // TODO: logging or do something
          }
        }
      }

    ParquetThriftOutputFormat.setThriftClass(job, classOf[TAccessLog])
    ParquetOutputFormat.setWriteSupportClass(job, classOf[TAccessLog])
    ParquetOutputFormat.setCompression(job, org.apache.parquet.hadoop.metadata.CompressionCodecName.GZIP)
    rdd.coalesce(numResultPartitions).saveAsNewAPIHadoopFile(
      outputUri,
      classOf[Void],
      classOf[TAccessLog],
      classOf[ParquetThriftOutputFormat[TAccessLog]],
      job.getConfiguration
    )
  }
}
