package project.ucsd.pages

import kotlinx.html.*

fun HTML.index() {
    head {
        title("Hello!!!")
    }
    body {
        div {
            +"Yeet"
        }
        button {
            +"Click Me!"
        }
        script(src = "/static/index.js") {}
    }
}