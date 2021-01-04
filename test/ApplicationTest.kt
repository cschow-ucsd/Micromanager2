package project.ucsd

import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner
import io.ktor.http.*
import io.ktor.server.testing.*
import project.ucsd.mm2.HoconConfig
import project.ucsd.mm2.module
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() {
        withTestApplication({ module() }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("HELLO WORLD!", response.content)
            }
        }
    }

    @Test
    fun dynamodbLocal() {
        val port = HoconConfig.dbEndpoint.substringAfterLast(':')
        val server = ServerRunner.createServerFromCommandLineArgs(
            arrayOf("-inMemory", "-port", port)
        )
        server.start()
        val endpointConfig = AwsClientBuilder.EndpointConfiguration(HoconConfig.dbEndpoint, HoconConfig.dbRegion)
        val ddbClient = AmazonDynamoDBClientBuilder.standard()
            .withEndpointConfiguration(endpointConfig)
            .build()
        println(ddbClient.listTables())
        server.stop()
    }
}
