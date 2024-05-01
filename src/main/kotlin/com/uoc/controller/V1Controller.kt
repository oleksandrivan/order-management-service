package com.uoc.controller;

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.uoc.domain.*
import com.uoc.service.OrderService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import reactor.core.publisher.Mono

@Controller("/v1")
class V1Controller(
    private val orderService: OrderService,
) {

    private val mapper = jacksonObjectMapper()

    @Post("/orders")
    fun createOrder(@Body request: String): Mono<HttpResponse<String>> {
        val createRequest = mapper.readValue(request, CreateOrderRequest::class.java)
        val result = orderService.createOrder(createRequest.toDomain())
        return result.fold(
            onSuccess = { HttpResponse.created(mapper.writeValueAsString(OrderSuccessResponse(it))) },
            onError = { HttpResponse.serverError(mapper.writeValueAsString(OrderFailureResponse(it.message!!))) }
        )
    }

    @Patch("/orders/{orderId}")
    fun updateOrder(@PathVariable orderId: OrderId, @Body request: String): Mono<HttpResponse<String>> {
        val updateOrderRequest = mapper.readValue(request, UpdateOrderRequest::class.java)
        val result = orderService.updateOrder(orderId, updateOrderRequest.toDomain())
        return result.fold(
            onSuccess = { HttpResponse.ok() },
            onError = { HttpResponse.serverError(it.message!!) }
        )
    }

    companion object {
        fun CreateOrderRequest.toDomain() = Order(
            orderId = OrderId(),
            customerId = CustomerId(1),
            items = items.map { OrderItem(it.key, it.value) },
            shippingAddress = AddressId(1)
        )
        fun UpdateOrderRequest.toDomain() = OrderStatus.valueOf(status)

        fun <T, R> Mono<T>.fold(onSuccess: (T) -> R, onError: (Throwable) -> R): Mono<R> {
            return this
                .map(onSuccess)
                .onErrorResume { error -> Mono.just(onError(error)!!) }
        }
    }
}


sealed class OrderResponse
data class OrderSuccessResponse(val orderId: OrderId): OrderResponse()
data class OrderFailureResponse(val message: String): OrderResponse()

data class CreateOrderRequest(val items: Map<String, Int>)
data class UpdateOrderRequest(val status: String)
