package br.com.orangetalent05.pixkeymaneger.controllers

import br.com.orangetalent05.PixKeymanagerGrpcServiceGrpc
import br.com.orangetalent05.PixKeymanagerRemoveGrpcServiceGrpc.PixKeymanagerRemoveGrpcServiceBlockingStub
import br.com.orangetalent05.RemoveChavePixRequest
import br.com.orangetalent05.pixkeymaneger.shared.GrpcClientFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.mock
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RemoveChavePixControllerTest() {

    @field:Inject
    lateinit var removeGrpc: PixKeymanagerRemoveGrpcServiceBlockingStub

    @field:Inject
    @field: Client("/")
    lateinit var client: HttpClient

    @Test
    fun `deve remover uma chave pix`() {

        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val respostaGrpc = removeGrpc.remove(RemoveChavePixRequest.newBuilder()
            .setClientId(clienteId)
            .setPixId(pixId)
            .build())

        given(removeGrpc.remove(Mockito.any())).willReturn(respostaGrpc)

        val request = HttpRequest.DELETE<Any>("/api/v1/clientes/$clienteId/pix/$pixId")
        val response = client.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.OK  , response.status)
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun deletaChave() = mock(PixKeymanagerRemoveGrpcServiceBlockingStub::class.java)
    }
}