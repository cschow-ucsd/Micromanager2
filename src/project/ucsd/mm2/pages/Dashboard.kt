package project.ucsd.mm2.pages

import kotlinx.html.*

fun HTML.dashboard() {
    head {
        title("Dashboard!!!")
    }
    body {
        div {
            +"Dashboard"
        }
        script(src = "/static/dashboard.js") {}
    }
}