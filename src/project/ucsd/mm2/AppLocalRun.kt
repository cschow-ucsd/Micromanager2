package project.ucsd.mm2

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner
import io.ktor.server.engine.*
import io.ktor.server.netty.*

object AppLocalRun {
    @JvmStatic
    fun main(args: Array<String>) {
        // DynamoDB Local setup
        val ddbPort = "8000"
        val ddb = ServerRunner.createServerFromCommandLineArgs(
            arrayOf("-inMemory", "-port", ddbPort)
        )
        ddb.start()

        // set local endpoint for DynamoDB client
        System.setProperty("DYNAMODB_ENDPOINT", "http://localhost:$ddbPort")
        System.setProperty("DYNAMODB_REGION", "us-west-1")

        // start Ktor server
        embeddedServer(Netty, port = 8080) {
            module()
        }.apply {
            addShutdownHook { ddb.stop() }
            start(false)
        }
    }
}
