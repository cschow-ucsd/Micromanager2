package project.ucsd.mm2

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import project.ucsd.mm2.graphql.routeGraphQL
import project.ucsd.mm2.pages.routePages
import project.ucsd.mm2.solver.ScheduleSolver
import project.ucsd.mm2.solver.UnsolvedRequest

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Locations)

    install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
    }

    install(ContentNegotiation) {
        gson {
        }
    }

    routing {
        routePages()
        routeGraphQL()

        route("/api") {
            get("/yeet") {
                call.respond("YEEEEEEEEEEEEEEEET")
            }
            post("/solve") {
                val solveRequest = call.receive<UnsolvedRequest>()
                val status = ScheduleSolver.solve(solveRequest)
                call.respond(HttpStatusCode.Accepted, status)
            }
//            webSocket("/status") {
//                TODO("Use websocket to notify client on solve status.")
//            }
        }

        static("/static") {
            resources("static")
            resources("js")
        }
    }
}
