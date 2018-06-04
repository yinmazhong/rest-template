package bindong.utils.db

/**
  * Created by xubanxian on 17-6-15.
  * more comments,less complains.
  */
  case class DatabaseInfo(url: String,
                          user: String,
                          password: String,
                          driver: String,
                          dbName: String,
                          dbConfigs: List[String] = Nil)

  object RedShiftDatabaseInfo{
    def apply(host:String,
             port:Int,
             dbName:String,
             user:String,
             password:String,
             driver: String
             ):DatabaseInfo = {
      DatabaseInfo(s"jdbc:redshift://$host:$port/$dbName", user, password, driver, dbName)
    }
  }
object MysqlDatabaseInfo{
  def apply(host: String = "localhost",
            port: Int = 3306,
            dbName: String = "dev",
            user: String = "root",
            password: String = "admin",
            driver: String = "com.mysql.jdbc.Driver"): DatabaseInfo = {
    DatabaseInfo(s"jdbc:mysql://$host:$port/$dbName", user, password, driver, dbName)
  }
}

object DatabaseConfig{
  var redShift: DatabaseInfo = null
}