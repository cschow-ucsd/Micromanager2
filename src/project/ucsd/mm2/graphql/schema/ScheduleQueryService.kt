package project.ucsd.mm2.graphql.schema

import project.ucsd.mm2.graphql.ApplicationCallContext

class ScheduleQueryService {

    fun scheduleById(
        context: ApplicationCallContext,
        id: String
    ) {
        TODO("Retrieve schedule by ID")
    }

    fun schedules(
        page: Int? = null,
        size: Int? = null
    ) {
        if (size != null) TODO("Set max page size")
        if (page != null) TODO("set page number")
        TODO("Retrieve a list of schedules based on page")
    }
}