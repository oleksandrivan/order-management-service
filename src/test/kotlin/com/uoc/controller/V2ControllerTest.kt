package com.uoc.controller

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.util.*


class V2ControllerTest {

    private fun getProperties(): Map<String, Any> {
        return Collections.singletonMap(
            "micronaut.http.services.database-proxy.url",
            wireMock.baseUrl()
        )
    }

    @Test
    fun testStoreOrder() {
        ApplicationContext.run(EmbeddedServer::class.java, getProperties()).use { server ->
            wireMock.stubFor(
                WireMock.post("/v1/orders")
                    .willReturn(
                        WireMock.aResponse()
                            .withStatus(201)
                            .withBody("""
                                {
                                  "orderId": "069738cb-1234-4d28-964d-5bcb41d48943"
                                }
                            """.trimIndent())
                    )
            )
            val json = """
            {
                "items": {
                    "ProductA": 1,
                    "ProductB": 2
                }
            }
            """
            val client = HttpClient.create(server.url)
            val request = HttpRequest.POST("/v2/orders", json)
            val response = client.toBlocking().exchange(request, String::class.java)
            assert(response.status.code == 201)
            assert(response.body().contains("orderId"))
            assert(response.body().contains("069738cb-1234-4d28-964d-5bcb41d48943"))
        }
    }

    @Test
    fun testUpdateOrderStatus() {
        ApplicationContext.run(EmbeddedServer::class.java, getProperties()).use { server ->
            wireMock.stubFor(
                WireMock.patch("/v1/orders/069738cb-1234-4d28-964d-5bcb41d48943")
                    .willReturn(
                        WireMock.aResponse()
                            .withStatus(200)
                    )
            )
            val json = """
            {
                "status": "PREPARING"
            }
            """
            val client = HttpClient.create(server.url)
            val request = HttpRequest.PATCH("/v2/orders/069738cb-1234-4d28-964d-5bcb41d48943", json)
            val response = client.toBlocking().exchange(request, String::class.java)
            assert(response.status.code == 200)
        }
    }

    companion object {
        @JvmStatic
        @RegisterExtension
        var wireMock: WireMockExtension = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build()
    }
}
