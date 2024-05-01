package com.uoc.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.uoc.client.DatabaseProxyClient
import com.uoc.controller.V1Controller.Companion.fold
import com.uoc.controller.V1Controller.Companion.toDomain
import com.uoc.domain.OrderId
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import reactor.core.publisher.Mono

@Controller("/v2")
class V2Controller(
    private val databaseProxyClient: DatabaseProxyClient
) {
    private val mapper = jacksonObjectMapper()

    @Post("/orders")
    fun createOrder(@Body request: String): Mono<HttpResponse<String>> {
        val createRequest = mapper.readValue(request, CreateOrderRequest::class.java)
        val result = databaseProxyClient.createOrder(createRequest.toDomain())
        return result.fold(
            onSuccess = { HttpResponse.created(mapper.writeValueAsString(OrderSuccessResponse(it))) },
            onError = { HttpResponse.serverError(mapper.writeValueAsString(OrderFailureResponse(it.message!!))) }
        )
    }

    @Patch("/orders/{orderId}")
    fun updateOrder(@PathVariable orderId: OrderId, @Body request: String): Mono<HttpResponse<String>> {
        val updateOrderRequest = mapper.readValue(request, UpdateOrderRequest::class.java)
        val result = databaseProxyClient.updateOrder(orderId, updateOrderRequest.toDomain())
        return result.fold(
            onSuccess = { HttpResponse.ok() },
            onError = { HttpResponse.serverError(it.message!!) }
        )
    }
}
