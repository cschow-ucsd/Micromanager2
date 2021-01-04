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

    // primary key & attributes
    val keySchema = listOf(
        KeySchemaElement("user_pk", KeyType.HASH), // Partition Key
        KeySchemaElement("schedule_sk", KeyType.RANGE) // Sort Key
    )
    val attributeDefinitions = listOf(
        AttributeDefinition("user_pk", ScalarAttributeType.S),
        AttributeDefinition("schedule_sk", ScalarAttributeType.S),
        AttributeDefinition("created_at", ScalarAttributeType.S)
    )
    val provisionedThroughput = ProvisionedThroughput(10L, 10L)

    // local secondary index
    val indexKeySchema = listOf(
        KeySchemaElement("user_pk", KeyType.HASH), // Partition Key (secondary index)
        KeySchemaElement("created_at", KeyType.RANGE) // Sort Key (secondary index)
    )
    val nonKeyAttributes = listOf<String>("name") // TODO: Add non-key attributes to be projected to secondary index
    val projection = Projection().withProjectionType(ProjectionType.INCLUDE)
        .withNonKeyAttributes(nonKeyAttributes)
    val localSecondaryIndex = LocalSecondaryIndex()
        .withIndexName("created_at_index")
        .withKeySchema(indexKeySchema)
        .withProjection(projection)

    // create table request with primary key, attributes, & local secondary index
    val createTableRequest = CreateTableRequest()
        .withTableName(tableName)
        .withProvisionedThroughput(provisionedThroughput)
        .withAttributeDefinitions(attributeDefinitions)
        .withKeySchema(keySchema)
        .withLocalSecondaryIndexes(localSecondaryIndex)
    val table = createTable(createTableRequest)
    table.waitForActive()
    log.info("Success. DynamoDB table status: ${table.description.tableStatus}")
    return table
}