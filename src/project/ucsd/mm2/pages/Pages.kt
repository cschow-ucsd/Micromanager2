package project.ucsd.mm2.pages

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.routing.*
import kotlinx.html.HTML


fun Routing.routePages() {

    // general pages
    val routes = listOf(
        "/" to HTML::index,
        "/dashboard" to HTML::dashboard
    )
    routes.forEach { (route, html) ->
        get(route) {
            call.respondHtml(HttpStatusCode.OK, html)
        }
    }

    // dynamic routes
    get("/view") { // view schedule by ID
        val id = call.parameters["id"]
        TODO("Retrieve solved schedule from DB, display in webpage")
    }
}