package bindong.utils.db

import bindong.common.DataWareHouseWebConfig

/**
  * Created by xubanxian on 17-6-16.
  * more comments,less complains.
  */
object DbAccessTest {
  def main(args: Array[String]): Unit = {
    DataWareHouseWebConfig
    println("-------------------")
    //val res = new RedshiftSqlHelper("show tables").query
    val res = new RedshiftSqlHelper("select 1 as a,count(*) as count from userinfo;").query
   /* val resultSet = res.get
    var resByGetString:String= null
    while (resultSet.next()){
      resByGetString = resultSet.getString("count")
    }*/
    val resVal = res.get.value
    println(s"res: $res")
    println(s"resVal: $resVal")
    //println(s"res: $resByGetString")
  }
}
