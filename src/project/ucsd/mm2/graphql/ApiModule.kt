package project.ucsd.mm2.graphql

import com.expediagroup.graphql.SchemaGeneratorConfig
import com.expediagroup.graphql.TopLevelObject
import com.expediagroup.graphql.toSchema
import graphql.ExecutionInput
import graphql.GraphQL
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import project.ucsd.mm2.graphql.schema.ScheduleMutationService
import project.ucsd.mm2.graphql.schema.ScheduleQueryService

fun Application.installGraphQL() {
    val config = SchemaGeneratorConfig(listOf("project.ucsd.mm2.graphql"))
    val queries = topLevelObjects(ScheduleQueryService())
    val mutations = topLevelObjects(ScheduleMutationService())
    val schema = toSchema(config, queries, mutations)
    val graphQL = GraphQL.newGraphQL(schema).build()

    suspend fun ApplicationCall.executeQuery() {
        val request = receive<GraphQLRequest>()
        val executionInput = ExecutionInput.newExecutionInput()
            .context(ApplicationCallContext(this))
            .query(request.query)
            .operationName(request.operationName)
            .variables(request.variables)
            .build()
        val output = graphQL.execute(executionInput)
        respond(output)
    }

    routing {
        route("/graphql") {
            get {
                call.executeQuery()
            }
            post {
                call.executeQuery()
            }
        }
    }
}

private fun topLevelObjects(
    vararg elements: Any
): List<TopLevelObject> = elements.map { TopLevelObject(it) }