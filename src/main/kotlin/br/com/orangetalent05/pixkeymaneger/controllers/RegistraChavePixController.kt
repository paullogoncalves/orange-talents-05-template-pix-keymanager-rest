package br.com.orangetalent05.pixkeymaneger.controllers

import br.com.orangetalent05.PixKeymanagerGrpcServiceGrpc
import br.com.orangetalent05.pixkeymaneger.dtos.NovaChaveRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject
import javax.validation.Valid

@Validated
@Controller("api/v1/clientes/{clientId}")
class RegistraChavePixController(@Inject private val registraClient: PixKeymanagerGrpcServiceGrpc.PixKeymanagerGrpcServiceBlockingStub) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Post("/pix")
    fun insert(clientId:UUID, @Valid @Body request: NovaChaveRequest):HttpResponse<Any> {

        LOGGER.info("$clientId criando uma nova cahve pix com $request")
        val response = registraClient.registra(request.toGrpc(clientId))

        return HttpResponse.created(location(clientId, response.pixId ))
    }

    private fun location(clienteId: UUID, pixId: String) = HttpResponse
        .uri("/api/v1/clientes/$clienteId/pix/${pixId}")
}