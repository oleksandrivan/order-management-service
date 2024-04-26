package com.uoc.controller

import com.uoc.AbstractIntegrationTest
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import jakarta.inject.Inject
import org.junit.jupiter.api.Test

class V1ControllerTest: AbstractIntegrationTest() {

    @Inject
    @Client("/")
    lateinit var client: HttpClient

    @Test
    fun testCreateAndUpdateOrder() {
        val json = """
            {
                "items": {
                    "ProductA": 1,
                    "ProductB": 2
                }
            }
        """

        val request = HttpRequest.POST("/v1/orders", json)
        val response = client.toBlocking().exchange(request, String::class.java)
        val uuidRegex = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}".toRegex()
        assert(response.status.code == 201)
        assert(response.body().contains("orderId"))
        assert(response.body().contains(uuidRegex))

        val orderUuid = uuidRegex.find(response.body())!!.value

        //Update order status
        val updateJson = """
            {
                "status": "PREPARING"
            }
        """
        val updateRequest = HttpRequest.PATCH("/v1/orders/$orderUuid", updateJson)
        val updateResponse = client.toBlocking().exchange(updateRequest, String::class.java)
        assert(updateResponse.status.code == 200)
    }

}
