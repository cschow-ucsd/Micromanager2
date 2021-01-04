package project.ucsd.mm2.graphql.schema

import com.amazonaws.services.dynamodbv2.document.PrimaryKey
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap
import project.ucsd.mm2.dynamodb.DynamoDBModule
import project.ucsd.mm2.graphql.ApplicationCallContext
import project.ucsd.mm2.graphql.schema.model.MmSchedule
import project.ucsd.mm2.solver.SolveStatus

class ScheduleQueryService {

    fun scheduleById(
        context: ApplicationCallContext,
        userId: String, // Placeholder, to be changed
        scheduleId: String
    ): MmSchedule {
        val table = DynamoDBModule.scheduleTable
        val key = PrimaryKey()
        key.addComponent("userPk", userId)
        key.addComponent("itemSk", scheduleId)
        val item = table.getItem(key)
        return MmSchedule(updatedAt = item.getString("updatedAt"), name = item.getString("name"),
            events = item.getList("events"))
    }

    fun schedules(
        userId: String, //Placeholder to be changed
        size: Int? = 10
    ): List<MmSchedule> {
        val table = DynamoDBModule.scheduleTable
        val spec = QuerySpec()
            .withKeyConditionExpression("userPk = :upk")
            .withValueMap(
                ValueMap().withString(":upk", userId)
            )
            .withMaxPageSize(size)
        val items = table.query(spec)
        val iterator = items.iterator()
        val itemList = mutableListOf<MmSchedule>()
        while(iterator.hasNext()){
            val item = iterator.next()
            itemList.add(MmSchedule(updatedAt = item.getString("updatedAt"),
                name = item.getString("name"), events = item.getList("events")))
        }
        return itemList.toList()
    }
}