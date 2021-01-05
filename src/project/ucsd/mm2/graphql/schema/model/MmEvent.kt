package project.ucsd.mm2.graphql.schema.model

data class MmEvent(
    val name: String,
    val startTime: Long?,
    val endTime: Long?,
    val duration: Long?
)