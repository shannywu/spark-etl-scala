lazy val commonSettings = Seq(
    assemblyMergeStrategy in assembly := {
        case PathList("META-INF", "maven","org.slf4j","slf4j-api", ps) if ps.startsWith("pom") => MergeStrategy.discard
        case x =>
            val oldStrategy = (assemblyMergeStrategy in assembly).value
            oldStrategy(x)
    }
)

name := "spark-etl-scala"
scalaVersion := "2.11.12"
version := sys.env.get("PACKAGE_REVISION").getOrElse("latest")
target in assembly := file("target/")

libraryDependencies ++= {
    val sparkVer = "2.2.1"
    Seq(
        "org.apache.spark" %% "spark-core" % sparkVer % "provided" withSources(),
        "org.apache.hadoop" % "hadoop-client" % "2.7.0" % "provided" excludeAll(
          ExclusionRule(organization = "org.jboss.netty"),
          ExclusionRule(organization = "io.netty"),
          ExclusionRule(organization = "org.eclipse.jetty"),
          ExclusionRule(organization = "org.mortbay.jetty"),
          ExclusionRule(organization = "org.ow2.asm"),
          ExclusionRule(organization = "asm")
        ),
        "org.apache.parquet" % "parquet" % "1.7.0",
        "org.apache.parquet" % "parquet-thrift" % "1.7.0" excludeAll(
          ExclusionRule(organization = "com.hadoop.gplcompression")
        ),
        "org.apache.thrift" % "libthrift" % "0.10.0",
        "com.typesafe.play" %% "play-json" % "2.3.10"
    )
}

resolvers ++= Seq(
    "Twitter" at "https://maven.twttr.com/"
)

import com.intenthq.sbt.ThriftPlugin._
