package br.com.orangetalent05.pixkeymaneger.controllers

import br.com.orangetalent05.*
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
internal class ConsultaDetalheChaveControllerTest() {

    @field:Inject
    lateinit var grpcConsultaClient: PixKeymanagerConsultaGrpcServiceGrpc.PixKeymanagerConsultaGrpcServiceBlockingStub

    @field:Inject
    lateinit var grpcListaClient: PixKeymanagerListaGrpcServiceGrpc.PixKeymanagerListaGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun `deve carregar o detalhamento de uma chave pix`() {

        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        given(grpcConsultaClient.busca(Mockito.any())).willReturn(cunsultaChavePixResponse(clienteId, pixId))

        val request = HttpRequest.GET<Any>("/api/v1/clientes/$clienteId/pix/$pixId")
        val response = client.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull((response.body()))
    }

    @Test
    fun `deve carregar uma lista de chave pix`() {

        val clienteId = UUID.randomUUID().toString()

        given(grpcListaClient.lista(Mockito.any())).willReturn(listaChavePixResponse(clienteId))

        val request = HttpRequest.GET<Any>("/api/v1/clientes/$clienteId/pix/")
        val response = client.toBlocking().exchange(request, List::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
        assertEquals(response.body().size, 2)





    }

    private fun listaChavePixResponse(clientId: String): ListaChavesPixResponse {

        val chaveCpf = ListaChavesPixResponse.ChavePix.newBuilder()
            .setPixId(UUID.randomUUID().toString())
            .setTipo(TipoDeChave.CPF)
            .setChave("12345678900")
            .setTipoDeConta(TipoDeConta.CONTA_CORRENTE)
            .setCriadaEm(LocalDateTime.now().let {
                val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                Timestamp.newBuilder()
                    .setSeconds(createdAt.epochSecond)
                    .setNanos(createdAt.nano)
                    .build()
            })
            .build()

        val chaveCelular = ListaChavesPixResponse.ChavePix.newBuilder()
            .setPixId(UUID.randomUUID().toString())
            .setTipo(TipoDeChave.CELULAR)
            .setChave("557198764562")
            .setTipoDeConta(TipoDeConta.CONTA_CORRENTE)
            .setCriadaEm(LocalDateTime.now().let {
                val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                Timestamp.newBuilder()
                    .setSeconds(createdAt.epochSecond)
                    .setNanos(createdAt.nano)
                    .build()
            })
            .build()

        return ListaChavesPixResponse.newBuilder()
            .setClienteId(clientId)
            .addAllChaves(listOf(chaveCpf, chaveCelular))
            .build()

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
        fun consulta() = Mockito.mock(PixKeymanagerConsultaGrpcServiceGrpc.PixKeymanagerConsultaGrpcServiceBlockingStub::class.java)

        @Singleton
        fun lista() = Mockito.mock(PixKeymanagerListaGrpcServiceGrpc.PixKeymanagerListaGrpcServiceBlockingStub::class.java)
    }
}