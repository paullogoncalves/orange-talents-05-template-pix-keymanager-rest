package br.com.orangetalent05.pixkeymaneger.controllers

import br.com.orangetalent05.ConsultaChavePixResponse
import br.com.orangetalent05.PixKeymanagerConsultaGrpcServiceGrpc
import br.com.orangetalent05.TipoDeChave
import br.com.orangetalent05.TipoDeConta
import br.com.orangetalent05.pixkeymaneger.shared.GrpcClientFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ConsultaDetalheChaveControllerTest {

    @field:Inject
    lateinit var grpcClient: PixKeymanagerConsultaGrpcServiceGrpc.PixKeymanagerConsultaGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun `deve carregar o detalhamento de uma chave pix`() {

        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        given(grpcClient.busca(Mockito.any())).willReturn(cunsultaChavePixResponse(clienteId, pixId))

        val request = HttpRequest.GET<Any>("/api/v1/clientes/$clienteId/pix/$pixId")
        val response = client.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull((response.body()))
    }

    private fun cunsultaChavePixResponse(clientId: String, pixId: String):
            ConsultaChavePixResponse {

        return ConsultaChavePixResponse.newBuilder()
            .setClienteId(clientId)
            .setPixId(pixId)
            .setChave(ConsultaChavePixResponse.ChavePix.newBuilder()
                .setTipo(TipoDeChave.EMAIL)
                .setChave("paulo@gmail.com")
                .setConta(ConsultaChavePixResponse.ChavePix.ContaInfo.newBuilder()
                    .setTipo(TipoDeConta.CONTA_CORRENTE)
                    .setInstituicao("UNIBANCO ITAU")
                    .setNomeDoTitular("Rafael Ponte")
                    .setCpfDoTitular("63657520325")
                    .setAgencia("1218")
                    .setNumeroDaConta("123456")
                    .build())
                .setCriadaEm(LocalDateTime.now().let {
                    val createAt = it.atZone(ZoneId.of("UTC")).toInstant()
                    Timestamp.newBuilder()
                        .setSeconds(createAt.epochSecond)
                        .setNanos(createAt.nano)
                        .build()
                })).build()

    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubMock() = Mockito.mock(PixKeymanagerConsultaGrpcServiceGrpc.PixKeymanagerConsultaGrpcServiceBlockingStub::class.java)
    }
}