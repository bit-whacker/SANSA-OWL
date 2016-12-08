package net.sansa_stack.owl.spark.dataset

import net.sansa_stack.owl.common.parsing.{ManchesterSyntaxParsing, ManchesterSyntaxPrefixParsing}
import net.sansa_stack.owl.spark.rdd.ManchesterSyntaxOWLExpressionsRDDBuilder
import org.apache.spark.sql.SparkSession


object ManchesterSyntaxOWLExpressionsDatasetBuilder extends ManchesterSyntaxPrefixParsing {
  def build(spark: SparkSession, filePath: String): OWLExpressionsDataset = {
    buildAndGetDefaultPrefix(spark, filePath)._1
  }

  private[dataset] def buildAndGetDefaultPrefix(spark: SparkSession, filePath: String): (OWLExpressionsDataset, String) = {
    val res =
      ManchesterSyntaxOWLExpressionsRDDBuilder.buildAndGetPrefixes(spark.sparkContext, filePath)
    val rdd = res._1
    val defaultPrefix = res._2.getOrElse(ManchesterSyntaxParsing._empty, ManchesterSyntaxParsing.dummyURI)

    import spark.implicits._
    (spark.sqlContext.createDataset[String](rdd), defaultPrefix)
  }
}
