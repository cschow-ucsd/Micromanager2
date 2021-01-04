package project.ucsd.mm2.graphql.schema

import project.ucsd.mm2.graphql.ApplicationCallContext
import project.ucsd.mm2.solver.SolveStatus

class ScheduleMutationService {
    fun solve(
        context: ApplicationCallContext
    ): SolveStatus {
        TODO("Begin processing the unsolved schedule, immediately return info to subscribe to updates of this process")
    }

    fun deleteById(
        context: ApplicationCallContext,
        id: String
    ): SolveStatus {
        TODO("Delete the schedule with corresponding user and ID")
    }
}