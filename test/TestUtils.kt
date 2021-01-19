package project.ucsd

import io.ktor.application.*
import project.ucsd.mm2.AppLocalRun
import project.ucsd.mm2.module

fun Application.moduleWithLocalDB() {
    val server = AppLocalRun.setupLocalDB()
    server.start()
    module()
    environment.monitor.subscribe(ApplicationStopped) {
        server.stop()
    }
}