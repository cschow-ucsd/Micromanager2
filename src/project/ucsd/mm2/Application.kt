package project.ucsd.mm2

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.routing.*
import project.ucsd.mm2.dynamodb.installDynamoDB
import project.ucsd.mm2.graphql.installGraphQL
import project.ucsd.mm2.pages.installPages

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module() {
    install(Locations)

    install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
    }

    install(ContentNegotiation) {
        gson {
        }
    }

    installDynamoDB()
    installPages()
    installGraphQL()

    routing {
        static("/static") {
            resources("static")
            resources("js")
        }
    }
}
