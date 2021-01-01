package project.ucsd.pages

import io.ktor.application.*
import kotlinx.html.HTML

object Pages {
    val routes = listOf(
        "/" to HTML::index,
        "/dashboard" to HTML::dashboard
    )
}