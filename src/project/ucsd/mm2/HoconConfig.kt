package project.ucsd.mm2

import com.typesafe.config.ConfigFactory
import io.ktor.config.*

object HoconConfig : HoconApplicationConfig(ConfigFactory.load()) {
    operator fun get(key: String) = property(key).getString()

    val schedulerWorkers = this["scheduler.workers"].toInt()

    val dbTableName = this["aws.dynamodb.table_name"]
    val dbEndpoint = this["aws.dynamodb.endpoint"]
    val dbRegion = this["aws.dynamodb.region"]
}