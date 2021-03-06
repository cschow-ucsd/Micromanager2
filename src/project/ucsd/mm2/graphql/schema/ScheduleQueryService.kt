package project.ucsd.mm2.graphql.schema

import com.amazonaws.services.dynamodbv2.document.PrimaryKey
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap
import project.ucsd.mm2.dynamodb.DynamoDBModule
import project.ucsd.mm2.graphql.ApplicationCallContext
import project.ucsd.mm2.graphql.schema.model.MmSchedule

class ScheduleQueryService {

    fun scheduleById(
        context: ApplicationCallContext,
        userId: String, // Placeholder, to be changed
        scheduleId: String
    ): MmSchedule {
        val table = DynamoDBModule.scheduleTable
        val key = PrimaryKey()
        key.addComponent(DynamoDBModule.USER_PK, userId)
        key.addComponent(DynamoDBModule.ITEM_SK, scheduleId)
        val item = table.getItem(key)
        return MmSchedule(
            updatedAt = item.getString(DynamoDBModule.UPDATED_AT),
            name = item.getString(DynamoDBModule.NAME),
            events = item.getList(DynamoDBModule.EVENTS)
        )
    }

    fun schedules(
        userId: String, //Placeholder to be changed
        size: Int? = 10
    ): List<MmSchedule> {
        val table = DynamoDBModule.scheduleTable
        val spec = QuerySpec()
            .withKeyConditionExpression("${DynamoDBModule.USER_PK} = :upk")
            .withValueMap(
                ValueMap().withString(":upk", userId)
            )
            .withMaxPageSize(size)
        val items = table.query(spec)
        val iterator = items.iterator()
        val itemList = mutableListOf<MmSchedule>()
        while (iterator.hasNext()) {
            val item = iterator.next()
            itemList.add(
                MmSchedule(
                    updatedAt = item.getString(DynamoDBModule.UPDATED_AT),
                    name = item.getString(DynamoDBModule.NAME),
                    events = item.getList(DynamoDBModule.EVENTS)
                )
            )
        }
        return itemList.toList()
    }
}