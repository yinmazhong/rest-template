package bindong.service

import java.security.MessageDigest

import bindong.common.log.LogSupportProj
import com.redis.RedisClient
import spray.http.FormData
import spray.json.DefaultJsonProtocol

/**
  * Created by xubanxian on 17-6-14.
  * more comments,less complains.
  */
class MeasurementItemsService extends LogSupportProj{
 // val redis = new RedisClient("localhost",6379)
  val redis = new RedisClient("r-wz97548d23e47294.redis.rds.aliyuncs.com",6379,secret=Some("uZRI90bXDJfGYAUF"))
  def getMessageMd5(string: String): String = {
    try {
      val mD5 = MessageDigest.getInstance("MD5")
      mD5.update(string.getBytes())
      BigInt(1, mD5.digest()).toString(16)
    }
    catch {
      case e: Exception => e.printStackTrace()
        ""
    }
  }

  def save2Redis(measurementItemsFormdata: FormData): Option[String] = {
    val formDataField: Map[String, String] = measurementItemsFormdata.fields.toList.toMap
    val formDataFieldData = formDataField("data")

    /*val timestampInMillis = System.currentTimeMillis()
    val dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm")
    val timeStampInMinute = dateFormat.parse(dateFormat.format(timestampInMillis)).getTime

    logger.debug(s"[TimeStamp] CurrentTimeStamp in millis is $timestampInMillis and currentTimeStamp in minute $timeStampInMinute")
      val redisSetKey = "measurementItemsList"+timeStampInMinute
      logger.debug(s"[Save2Redis] Saving data to redis set key: $redisSetKey")*/
      try{
        //插入错误返回错误，成功的话返回当前list的长度
        redis.lpush("measurementItemsLists", formDataFieldData)
       /* redis.sadd(redisSetKey, formDataFieldData)
        redis.sadd("measurementItemsLists",redisSetKey)*/
        Some("success")
        }
    catch {
      case e:Exception => logger.error(s"[save2Redis ]${e.getMessage}")
        None
      case _ => logger.error(s"[save2Redis ] unkown error")
        None
    }
  }

  /*def upload2Redshift(measurementItemsFormdata: FormData): Boolean = {
    val measurementItems = formData2MeasurementItems(measurementItemsFormdata)
    if (measurementItems.UUID != null) {
      val createSql = s"create table if not exists measurementiteminfos(UUID varchar(200),scanDate varchar(200),scanId varchar(200),customerId varchar(200),customerInfo varchar(10240),measurementItemsNum Int,measurementItem varchar(10240),analysisReport varchar(10240))"
      val insertSql = s"insert into measurementiteminfos values('${measurementItems.UUID}','${measurementItems.scanDate}','${measurementItems.scanId}','${measurementItems.customerId}','${measurementItems.customerInfo}','${measurementItems.measurementItemsNum}','${measurementItems.mesurementItemInfos}','${measurementItems.analysisReport}')"
      new RedshiftSqlHelper(createSql).execute() && new RedshiftSqlHelper(insertSql).execute()
    } else false

  }*/

 /* protected def formData2MeasurementItems(formData: FormData): MeasurementItems = {
    val formDataField: Map[String, String] = formData.fields.toList.toMap
    val formDataFieldData = formDataField("data")
    val formDataFieldDataArray = formDataFieldData.split("\\|\\|")
    val (scanDate, scanId, customerId, customerInfoStr, measurementItemsNum, measurementItemInfoStr, uuIdStr, analysisReportStr) =
      (formDataFieldDataArray(0), formDataFieldDataArray(1), formDataFieldDataArray(2), formDataFieldDataArray(3), formDataFieldDataArray(4), formDataFieldDataArray(5), formDataFieldDataArray(6), formDataFieldDataArray(7))
    MeasurementItems(uuIdStr, scanDate, scanId, customerId, customerInfoStr, measurementItemsNum, measurementItemInfoStr, analysisReportStr)
  }*/
}


/*为了保证upload到达数据的有效性，
* 有必要即时检查上传数据的维度信息(粗略检查)，
* 并即时反馈给dlo扫描仪当前上传的消息有效与否。
* */
case class MeasurementItems(UUID: String, scanDate: String, scanId: String, customerId: String
                            , customerInfo: String, measurementItemsNum: String, mesurementItemInfos: String, analysisReport: String)


case class CustomerInfo(customerName: String, customerAge: Int, customerSex: Int
                        , customerNativePlace: String, customerHeight: Double
                        , customerPhoneNumber: String, customerEmail: String
                        , customerWeChat: String, advice: String)

case class MeasurementItemInfo(MeasurementItemName: String, measurementItemDetail: MeasurementItemDetail)

case class MeasurementItemDetail(left: Double, right: Double)


case class AnalysisReport(leftFootType: Int, leftFootInoutsideTurn: Int, leftFootInoutsideRotate: Int, rightFootType: Int, rightFootInoutsideTurn: Int, rightFootInoutsideRotate: Int)

object MeasurementItemsJsonImplicits extends DefaultJsonProtocol {
  implicit val CustomerInfoJson = jsonFormat9(CustomerInfo)
  implicit val MeasurementItemDetailJson = jsonFormat2(MeasurementItemDetail)
  implicit val MeasurementItemInfoJson = jsonFormat2(MeasurementItemInfo)
  implicit val AnalysisReportJson = jsonFormat6(AnalysisReport)
  implicit val MeasurementItemsJson = jsonFormat8(MeasurementItems)
}