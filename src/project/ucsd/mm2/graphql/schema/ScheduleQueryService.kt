package project.ucsd.mm2.graphql.schema

import project.ucsd.mm2.graphql.ApplicationCallContext
import project.ucsd.mm2.solver.SolveStatus

class ScheduleQueryService {

    fun scheduleById(
        context: ApplicationCallContext,
        id: String
    ): SolveStatus {
        TODO("Retrieve schedule by ID")
    }

    fun schedules(
        page: Int? = null,
        size: Int? = null
    ): SolveStatus {
        if (size != null) TODO("Set max page size")
        if (page != null) TODO("set page number")
        TODO("Retrieve a list of schedules based on page")
    }
}