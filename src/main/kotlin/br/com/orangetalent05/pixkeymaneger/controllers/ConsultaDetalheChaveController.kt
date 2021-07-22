package br.com.orangetalent05.pixkeymaneger.controllers

import br.com.orangetalent05.ConsultaChavePixRequest
import br.com.orangetalent05.ListaChavesPixRequest
import br.com.orangetalent05.PixKeymanagerConsultaGrpcServiceGrpc
import br.com.orangetalent05.PixKeymanagerListaGrpcServiceGrpc
import br.com.orangetalent05.pixkeymaneger.dtos.ChavePixResponse
import br.com.orangetalent05.pixkeymaneger.dtos.DetalheChavePixResponse
import br.com.orangetalent05.pixkeymaneger.dtos.PixIdRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*
import javax.validation.Valid

@Validated
@Controller("/api/v1/clientes/{clienteId}")
class ConsultaDetalheChaveController(
    private val consultaGrpc: PixKeymanagerConsultaGrpcServiceGrpc.PixKeymanagerConsultaGrpcServiceBlockingStub,
    private val listaGrpcClient: PixKeymanagerListaGrpcServiceGrpc.PixKeymanagerListaGrpcServiceBlockingStub,
) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Get("/pix/{pixId}")
    fun carrega(clienteId: UUID,
                pixId: UUID) : HttpResponse<Any> {

        LOGGER.info("[$clienteId] carrega chave pix por id: $pixId")
        val chaveResponse = consultaGrpc.busca(ConsultaChavePixRequest.newBuilder()
            .setPixId(ConsultaChavePixRequest.FiltroPorPixId.newBuilder()
                .setClienteId(clienteId.toString())
                .setPixId(pixId.toString())
                .build()).
            build())

        return HttpResponse.ok(DetalheChavePixResponse(chaveResponse))
    }

    @Get("/pix/")
    fun lista(clienteId: UUID) : HttpResponse<Any> {

        LOGGER.info("[$clienteId] listando chaves pix")
        val pix = listaGrpcClient.lista(ListaChavesPixRequest.newBuilder()
            .setClientId(clienteId.toString())
            .build())

        val chaves = pix.chavesList.map { ChavePixResponse(it) }
        return HttpResponse.ok(chaves)
    }
}