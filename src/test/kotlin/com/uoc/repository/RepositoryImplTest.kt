package com.uoc.repository

import com.uoc.AbstractIntegrationTest
import com.uoc.domain.*
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
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
        val customer = customer()
        val address = address()

        val customerResult = customerRepository.storeCustomer(customer)
        val addressResult = addressRepository.storeAddress(address)
        val orderResult = orderRepository.storeOrder(order)

        assertTrue(customerResult.isSuccess)
        assertTrue(addressResult.isSuccess)
        assertTrue(orderResult.isSuccess)
        val storedOrder = orderRepository.findOrder(order.orderId)
        assertNotNull(storedOrder)
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
