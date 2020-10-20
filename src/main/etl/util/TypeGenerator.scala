package etl.util

import types._
import play.api.libs.json.{JsObject, JsValue}
import java.util.ArrayList

object TypeGenerator {

  def genTIpDetail(json: JsValue): TIpDetail = {
    return new TIpDetail(
      (json \ "asnum").asOpt[Int].getOrElse(-1),
      (json \ "country_code").asOpt[String].getOrElse(""),
      (json \ "host").asOpt[String].getOrElse("")
    )
  }

  def genTRequestDetail(json: JsValue): TRequestDetail = {
    return new TRequestDetail(
      (json \ "method").asOpt[String].getOrElse(""),
      (json \ "url" \ "scheme").asOpt[String].getOrElse(""),
      (json \ "url" \ "host").asOpt[String].getOrElse(""),
      (json \ "url" \ "port").asOpt[Int].getOrElse(0),
      (json \ "url" \ "path").asOpt[String].getOrElse(""),
      TypeConverter.convertMap(json \ "url" \ "query"),
      (json \ "protocol").asOpt[String].getOrElse("")
    )
  }
}
