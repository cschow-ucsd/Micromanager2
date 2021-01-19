package project.ucsd

import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer
import io.ktor.http.*
import io.ktor.server.testing.*
import project.ucsd.mm2.AppLocalRun
import project.ucsd.mm2.module
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() {
        withTestApplication({ moduleWithLocalDB() }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }
}
