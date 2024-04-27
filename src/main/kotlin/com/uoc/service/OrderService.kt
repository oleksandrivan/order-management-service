package com.uoc.service

import com.uoc.domain.Order
import com.uoc.domain.OrderId
import com.uoc.domain.OrderStatus
import com.uoc.repository.OrderRepository
import jakarta.inject.Singleton
import reactor.core.publisher.Mono

interface OrderService {

    fun createOrder(order: Order): Mono<OrderId>
    fun updateOrder(orderId: OrderId, status: OrderStatus): Mono<OrderId>
}

@Singleton
class OrderServiceImpl(
    private val orderRepository: OrderRepository
): OrderService {

    override fun createOrder(order: Order): Mono<OrderId> = orderRepository.storeOrder(order)

    override fun updateOrder(orderId: OrderId, status: OrderStatus): Mono<OrderId> =
        orderRepository.updateOrder(orderId, status)
}