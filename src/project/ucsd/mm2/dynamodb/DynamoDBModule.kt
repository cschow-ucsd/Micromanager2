package project.ucsd.mm2.dynamodb

import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.document.Table
import com.amazonaws.services.dynamodbv2.model.*
import io.ktor.application.*
import org.slf4j.Logger
import project.ucsd.mm2.HoconConfig

object DynamoDBModule {
    lateinit var scheduleTable: Table
}

fun Application.installDynamoDB() {
    val endpointConfig = AwsClientBuilder.EndpointConfiguration(HoconConfig.dbEndpoint, HoconConfig.dbRegion)
    val ddbClient = AmazonDynamoDBClientBuilder.standard()
        .withEndpointConfiguration(endpointConfig)
        .build()
    val ddb = DynamoDB(ddbClient)
    DynamoDBModule.scheduleTable = ddb.getTableOrCreate(HoconConfig.dbTableName, environment.log)
}

private fun DynamoDB.getTableOrCreate(
    tableName: String,
    log: Logger
): Table {
    return try {
        try {
            createTable(tableName, log)
        } catch (e: ResourceInUseException) {
            log.info("Table already exists; getting DynamoDB table.")
            getTable(tableName)
        }
    } catch (e: Exception) {
        log.error("Unable to get or create DynamoDB table!")
        throw e
    }
}

private fun DynamoDB.createTable(
    tableName: String,
    log: Logger
): Table {
    log.info("Attempting to create DynamoDB table; please wait...")
    val table = createTable(
        tableName,
        listOf(
            KeySchemaElement("user_pk", KeyType.HASH), // Partition Key
            KeySchemaElement("schedule_sk", KeyType.RANGE) // Sort Key
        ),
        listOf(
            AttributeDefinition("user_pk", ScalarAttributeType.S),
            AttributeDefinition("schedule_sk", ScalarAttributeType.S)
        ),
        ProvisionedThroughput(10L, 10L)
    )
    table.waitForActive()
    log.info("Success. DynamoDB table status: ${table.description.tableStatus}")
    return table
}