package project.ucsd.mm2

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer
import io.ktor.server.engine.*
import io.ktor.server.netty.*

object AppLocalRun {
    @JvmStatic
    fun main(args: Array<String>) {
        val ddb = setupLocalDB()
        ddb.start()

        embeddedServer(Netty, port = 8080) {
            module()
        }.apply {
            addShutdownHook { ddb.stop() }
            start(false)
        }
    }

    fun setupLocalDB(): DynamoDBProxyServer {
        val ddbPort = HoconConfig.dbTestPort
        return ServerRunner.createServerFromCommandLineArgs(
            arrayOf("-inMemory", "-port", ddbPort)
        )
    }
}
