package project.ucsd.mm2

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer
import io.ktor.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.slf4j.LoggerFactory

object AppLocalRun {
    @JvmStatic
    fun main(args: Array<String>) {
        val dbLogger = LoggerFactory.getLogger(AppLocalRun.javaClass)
        val ddb = setupLocalDB()
        ddb.start()
        dbLogger.info("Local DynamoDB started!")

        embeddedServer(Netty, port = 8080) {
            module()
            environment.monitor.subscribe(ApplicationStopped) {
                ddb.stop()
                dbLogger.info("Local DynamoDB stopped.")
            }
        }.start(wait = false)
    }

    fun setupLocalDB(): DynamoDBProxyServer {
        val ddbPort = HoconConfig.dbTestPort
        return ServerRunner.createServerFromCommandLineArgs(
            arrayOf("-inMemory", "-port", ddbPort)
        )
    }
}
