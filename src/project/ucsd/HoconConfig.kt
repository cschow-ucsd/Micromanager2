package project.ucsd

import com.typesafe.config.ConfigFactory
import io.ktor.config.*

object HoconConfig : HoconApplicationConfig(ConfigFactory.load()) {
    operator fun get(key: String) = property(key).getString()

    val schedulerWorkers = this["scheduler.workers"].toInt()
}