package com.uoc.repository

import com.uoc.domain.*
import com.uoc.jooq.tables.records.OrderRecord
import com.uoc.jooq.tables.references.ORDER
import com.uoc.jooq.tables.references.ORDERITEM
import jakarta.inject.Singleton
import org.jooq.DSLContext
import org.jooq.impl.DSL
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

interface OrderRepository {

    fun findOrder(orderId: OrderId): Mono<Order>
    fun storeOrder(order: Order): Mono<OrderId>
    fun updateOrder(orderId: OrderId, orderStatus: OrderStatus): Mono<OrderId>
}

@Singleton
class OrderRepositoryImpl(
    private val dslContext: DSLContext
) : OrderRepository {

    override fun findOrder(orderId: OrderId): Mono<Order> {
        val items: Flux<OrderItem> = Flux.from(dslContext.select()
            .from(ORDERITEM)
            .where(ORDERITEM.ORDERID.eq(orderId.value)))
            .map { record -> OrderItem(record.get(ORDERITEM.PRODUCTID)!!, record.get(ORDERITEM.QUANTITY)!!) }

        val order = Mono.from(dslContext.select()
            .from(ORDER)
            .where(ORDER.ID.eq(orderId.value)))
            .map { record -> record.into(OrderRecord::class.java) }

        return order.toDomain(items)
    }


    override fun storeOrder(order: Order): Mono<OrderId> {
        val orderRecord = Mono.from(
            dslContext.insertInto(ORDER)
                .set(ORDER.ID, order.orderId.value)
                .set(ORDER.CUSTOMERID, order.customerId.value)
                .set(ORDER.SHIPPINGADDRESS, order.shippingAddress.value)
                .set(ORDER.STATUS, com.uoc.jooq.enums.OrderStatus.valueOf(order.status.name))
                .set(ORDER.CREATEDAT, order.createdAt)
                .set(ORDER.UPDATEDAT, order.updatedAt)
        )

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

        return orderRecord.flatMap { orderResult ->
            orderItemsResult.collectList().flatMap { orderItemsResult ->
                if (orderResult == 1 && orderItemsResult.sum() == order.items.size) {
                    Mono.just(order.orderId)
                } else {
                    Mono.error(RuntimeException("Failed to store order"))
                }
            }
        }
    }

    override fun updateOrder(orderId: OrderId, orderStatus: OrderStatus): Mono<OrderId> {
        val orderResult = Mono.from(dslContext.update(ORDER)
            .set(ORDER.STATUS, com.uoc.jooq.enums.OrderStatus.valueOf(orderStatus.name))
            .set(ORDER.UPDATEDAT, LocalDateTime.now())
            .where(ORDER.ID.eq(orderId.value)))
        return orderResult.handle { result, sink ->
            if (result == 1) {
                sink.next(orderId)
            } else {
                sink.error(RuntimeException("Failed to update order"))
            }
        }
    }

    companion object{

        private fun Mono<OrderRecord>.toDomain(itemsFlux: Flux<OrderItem>): Mono<Order> {
            return this.flatMap { record ->
                itemsFlux.collectList().map { items ->
                    with(record) {
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
    }
}
