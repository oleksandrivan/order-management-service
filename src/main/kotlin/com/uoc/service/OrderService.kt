package com.uoc.service

import com.uoc.domain.Order
import com.uoc.domain.OrderId
import com.uoc.domain.OrderStatus
import com.uoc.repository.OrderRepository
import jakarta.inject.Singleton

interface OrderService {

    fun createOrder(order: Order): Result<OrderId>
    fun updateOrder(orderId: OrderId, status: OrderStatus): Result<OrderId>
}

@Singleton
class OrderServiceImpl(
    private val orderRepository: OrderRepository
): OrderService {

    override fun createOrder(order: Order): Result<OrderId> = runCatching {
        orderRepository.storeOrder(order).getOrThrow()
    }

    override fun updateOrder(orderId: OrderId, status: OrderStatus): Result<OrderId> = runCatching {
        orderRepository.updateOrder(orderId, status).getOrThrow()
    }
}