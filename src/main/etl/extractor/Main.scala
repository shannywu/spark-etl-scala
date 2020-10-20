package com.kkbox.pod.etl.extractor

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.hadoop.mapreduce.Job
import scala.util.Try

object Main {
  def main(args: Array[String]) {
    if (args.length < 3) {
      System.err.println("Usage: Main <input dir> <output dir> <extractor class>")
      System.exit(1)
    }
    val inputFile = args(0)
    val outputUri = args(1)
    val extractorClass = args(2)
    val fileSplitOutput = Try(args(3)).toOption.getOrElse("")

    val job = Job.getInstance()
    val conf = new SparkConf().setAppName("pod-spark-etl")
    val sc = new SparkContext(conf)

    val extractor = Class.forName(extractorClass).newInstance().asInstanceOf[ExtractorTrait]

    if (fileSplitOutput != "") {
      extractor.fileSplitOutput = fileSplitOutput
    }

    extractor.extract(sc, job, inputFile, outputUri)
  }
}
