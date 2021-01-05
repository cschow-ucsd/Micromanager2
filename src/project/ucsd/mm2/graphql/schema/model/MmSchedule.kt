package project.ucsd.mm2.graphql.schema.model

data class MmSchedule(
    val updatedAt: String,
    val name: String,
    val events: List<MmEvent>
)