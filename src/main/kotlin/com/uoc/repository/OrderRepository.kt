package com.uoc.repository

import com.uoc.domain.*
import com.uoc.jooq.tables.records.OrderRecord
import com.uoc.jooq.tables.records.OrderitemRecord
import com.uoc.jooq.tables.references.ORDER
import com.uoc.jooq.tables.references.ORDERITEM
import jakarta.inject.Singleton
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import javax.sql.DataSource

interface OrderRepository {

    fun findOrder(orderId: OrderId): Result<Order>
    fun storeOrder(order: Order): Result<OrderId>
    fun updateOrder(orderId: OrderId, orderStatus: OrderStatus): Result<OrderId>
}

@Singleton
class OrderRepositoryImpl(
    private val dslContext: DSLContext
) : OrderRepository {

    override fun findOrder(orderId: OrderId): Result<Order> {
        val items: Flux<OrderItem> = Flux.from(dslContext.select()
            .from(ORDERITEM)
            .where(ORDERITEM.ORDERID.eq(orderId.value)))
            .map { record -> OrderItem(record.get(ORDERITEM.PRODUCTID)!!, record.get(ORDERITEM.QUANTITY)!!) }

        val order = Mono.from(dslContext.select()
            .from(ORDER)
            .where(ORDER.ID.eq(orderId.value)))
            .map { record -> record.into(OrderRecord::class.java) }

        return Result.success(order.toDomain(items))
    }


    override fun storeOrder(order: Order): Result<OrderId> {
        val orderResult = Mono.from(dslContext.insertInto(ORDER)
            .set(ORDER.ID, order.orderId.value)
            .set(ORDER.CUSTOMERID, order.customerId.value)
            .set(ORDER.SHIPPINGADDRESS, order.shippingAddress.value)
            .set(ORDER.STATUS, com.uoc.jooq.enums.OrderStatus.valueOf(order.status.name))
            .set(ORDER.CREATEDAT, order.createdAt)
            .set(ORDER.UPDATEDAT, order.updatedAt))

        val itemsRecords = order.items.map { item ->
            DSL.row(order.orderId.value, item.productId, item.quantity)
        }
        val orderItemsResult = Flux.from(
            dslContext.insertInto(
                ORDERITEM,
                ORDERITEM.ORDERID,
                ORDERITEM.PRODUCTID,
                ORDERITEM.QUANTITY
            ).valuesOfRows(itemsRecords)
        )

        return if(orderResult.block()!! == 1 && orderItemsResult.collectList().block()!!.sum() == order.items.size){
            Result.success(order.orderId)
        } else{
            Result.failure(RuntimeException("Failed to store order"))
        }
    }

    override fun updateOrder(orderId: OrderId, orderStatus: OrderStatus): Result<OrderId> {
        val orderResult = Mono.from(dslContext.update(ORDER)
            .set(ORDER.STATUS, com.uoc.jooq.enums.OrderStatus.valueOf(orderStatus.name))
            .set(ORDER.UPDATEDAT, LocalDateTime.now())
            .where(ORDER.ID.eq(orderId.value)))
        return when (orderResult.block()!!) {
            1 -> Result.success(orderId)
            else -> Result.failure(RuntimeException("Failed to store customer"))
        }
    }

    companion object{

        private fun Mono<OrderRecord>.toDomain(itemsFlux: Flux<OrderItem>): Order {
            val record = this.block()!!
            val items = itemsFlux.collectList().block()!!
            return with(record){
                Order(
                    orderId = OrderId(id!!),
                    customerId = CustomerId(customerid!!),
                    items = items,
                    shippingAddress = AddressId(shippingaddress!!),
                    status = OrderStatus.valueOf(status!!.name),
                    createdAt = createdat!!,
                    updatedAt = updatedat!!
                )
            }
        }
    }
}
