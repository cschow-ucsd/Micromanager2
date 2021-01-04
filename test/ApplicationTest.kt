package project.ucsd

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
    fun yeet() {
        System.setProperty("sqlite4java.library.path", "native-libs")
        val port = "9090" //HoconConfig.dbEndpoint.substringAfterLast(':')
        val server = ServerRunner.createServerFromCommandLineArgs(
            arrayOf("-inMemory", "-port", port)
        )
        server.start()
        Thread.sleep(5000)
        server.stop()
    }
}
