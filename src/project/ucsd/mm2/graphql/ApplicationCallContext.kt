package project.ucsd.mm2.graphql

import com.expediagroup.graphql.execution.GraphQLContext
import io.ktor.application.*

data class ApplicationCallContext(
    val call: ApplicationCall
): GraphQLContext