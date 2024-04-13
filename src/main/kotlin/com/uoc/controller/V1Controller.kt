package com.uoc.controller;

import com.uoc.domain.OrderId
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Patch
import io.micronaut.http.annotation.PathVariable

@Controller("/v1")
class V1Controller {

    @Post("/orders")
    fun createOrder(): HttpResponse<OrderResponse> = HttpResponse.created(OrderResponse(OrderId()))

    @Patch("/orders/{orderId}")
    fun updateOrder(@PathVariable orderId: OrderId): HttpResponse<Unit> = HttpResponse.ok(Unit)

}

data class OrderResponse(val orderId: OrderId)
