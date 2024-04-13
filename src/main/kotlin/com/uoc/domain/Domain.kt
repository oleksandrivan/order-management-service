package com.uoc.domain

import java.time.LocalDateTime
import java.util.*

data class Order(
    val orderId: OrderId,
    val customerId: CustomerId,
    val items: List<OrderItem>,
    val shippingAddress: AddressId,
    var status: OrderStatus = OrderStatus.CREATED,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
)

@JvmInline
value class OrderId(val value: String = UUID.randomUUID().toString())

data class Customer(
    val customerId: CustomerId,
    val name: String,
    val email: String
)

@JvmInline
value class CustomerId(val value: Int)

data class OrderItem(
    val productId: String,
    val quantity: Int
)

data class Address(
    val addressId: AddressId,
    val street: String,
    val city: String,
    val state: String,
    val postalCode: String,
    val country: String
)

@JvmInline
value class AddressId(val value: Int)

enum class OrderStatus {
    CREATED,
    PREPARING,
    SHIPPED,
    DELIVERED,
    CANCELLED
}