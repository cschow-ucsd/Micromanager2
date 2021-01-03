package project.ucsd.mm2.graphql

data class GraphQLRequest(
    val query: String,
    val operationName: String?,
    val variables: Map<String, Any>?
)