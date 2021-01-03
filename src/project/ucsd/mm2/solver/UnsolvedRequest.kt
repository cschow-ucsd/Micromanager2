package project.ucsd.mm2.solver

// TODO: create Event classes
data class UnsolvedRequest(
    val scheduleName: String,
    val fixedEvents: List<String>,
    val toPlanEvents: List<String>,
    val userPreferences: String
)
