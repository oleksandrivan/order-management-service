package com.uoc.client

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.uoc.controller.CreateOrderRequest
import com.uoc.controller.OrderSuccessResponse
import com.uoc.controller.UpdateOrderRequest
import com.uoc.domain.*
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import jakarta.inject.Singleton
import reactor.core.publisher.Mono

interface DatabaseProxyClient {
    fun createOrder(order: Order): Mono<OrderId>
    fun updateOrder(orderId: OrderId, newOrderStatus: OrderStatus): Mono<OrderId>
}

@Singleton
class DatabaseProxyClientImpl(
    @param:Client(id = "database-proxy") private val httpClient: HttpClient,
) : DatabaseProxyClient {

    private val mapper = jacksonObjectMapper()

    override fun createOrder(order: Order): Mono<OrderId> {
        val uri = UriBuilder.of("/v1/orders").build()
        val json = mapper.writeValueAsString(CreateOrderRequest(order.items.associate { it.productId to it.quantity }))
        val request: HttpRequest<*> = HttpRequest.POST(uri, json)
        return Mono.from(httpClient.retrieve(request, String::class.java))
            .map { OrderId(mapper.readTree(it)["orderId"].asText()) }
    }

    override fun updateOrder(orderId: OrderId, newOrderStatus: OrderStatus): Mono<OrderId> {
        val uri = UriBuilder.of("/v1/orders").path(orderId.value).build()
        val json = mapper.writeValueAsString(UpdateOrderRequest(newOrderStatus.name))
        val request: HttpRequest<*> = HttpRequest.PATCH(uri, json)
        return Mono.from(httpClient.exchange(request, String::class.java))
            .map { orderId }
    }
}

