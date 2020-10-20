package etl.util

import play.api.libs.json.JsValue
import scala.util.Try

object TypeConverter {

    def convertMap(json: JsValue): java.util.HashMap[String, String] = {

        // convert the query of JsValue type to java.util.Map type
        // FIXME: look for a more elegant type conversion?
        val qry = (json).asOpt[Map[String, String]].getOrElse(null)
        val qryjmap = new java.util.HashMap[String, String]()
        if (qry != null) {
          for ((k, v) <- qry) {
            qryjmap.put(k, v)
          }
        }
        return qryjmap
    }

    def convertDateToText(json: JsValue): String = {
        val date = (json).asOpt[Long].getOrElse(0L).toString
        var formatDate = ""
        if (date != "0" && date.length() == 8) {
            formatDate = "%s-%s-%s".format(date.substring(0, 4), date.substring(4, 6), date.substring(6, 8))
        } else {
            formatDate = ""
        }
        return formatDate
    }

    def convertStringToInt(json: JsValue, default: Int): Int = {
        return Try((json).asOpt[String].getOrElse("").toInt).toOption.getOrElse(default)
    }

    def convertStringToDouble(json: JsValue, default: Double): Double = {
        return Try((json).asOpt[String].getOrElse("").toDouble).toOption.getOrElse(default)
    }

    def convertStringToLong(json: JsValue, default: Long): Long = {
         return Try((json).asOpt[String].getOrElse((json).asOpt[Long].getOrElse(default)).toString.toLong).toOption.getOrElse(default)
    }

    def convertToString(json: JsValue, default: String): String = {
        return (json).asOpt[String].getOrElse((json).asOpt[Int].getOrElse(default)).toString
    }
}
