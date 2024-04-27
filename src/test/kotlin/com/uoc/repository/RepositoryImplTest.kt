package com.uoc.repository

import com.uoc.AbstractIntegrationTest
import com.uoc.domain.*
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Test

@MicronautTest
class RepositoryImplTest : AbstractIntegrationTest() {

    @Inject
    lateinit var orderRepository: OrderRepository

    @Inject
    lateinit var addressRepository: AddressRepository

    @Inject
    lateinit var customerRepository: CustomerRepository

    @Test
    fun when_storeOrder_then_storesOrderAndRelated() {
        val order = order()
        val orderResult = orderRepository.storeOrder(order).block()!!

        val storedOrder = orderRepository.findOrder(order.orderId).block()!!
        with(storedOrder) {
            assert(orderId == orderResult)
            assert(customerId == CustomerId(1))
            assert(shippingAddress == AddressId(1))
            assert(items.size == 2)
        }

        val newStatus = OrderStatus.PREPARING
        val updateResult = orderRepository.updateOrder(order.orderId, newStatus).block()!!
        val updatedOrder = orderRepository.findOrder(order.orderId).block()!!
        with(updatedOrder) {
            assert(orderId == updateResult)
            assert(status == newStatus)
        }
    }

    private fun order(): Order {
        return Order(
            orderId = OrderId(),
            customerId = CustomerId(1),
            shippingAddress = AddressId(1),
            items = listOf(
                OrderItem(productId = "ProductA", quantity = 1),
                OrderItem(productId = "ProductB", quantity = 2)
            ),
        )
    }

    private fun customer(): Customer {
        return Customer(
            customerId = CustomerId(1),
            name = "John Doe",
            email = "jonh.doe@example.com",
        )
    }

    private fun address(): Address {
        return Address(
            addressId = AddressId(1),
            street = "123 Main St",
            city = "Springfield",
            state = "IL",
            postalCode = "62701",
            country = "USA",
        )
    }
}
