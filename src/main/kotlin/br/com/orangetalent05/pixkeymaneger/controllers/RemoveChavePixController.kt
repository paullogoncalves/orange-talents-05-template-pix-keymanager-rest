package br.com.orangetalent05.pixkeymaneger.controllers

import br.com.orangetalent05.PixKeymanagerRemoveGrpcServiceGrpc
import br.com.orangetalent05.RemoveChavePixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject


@Controller("api/v1/clientes/{clientId}")
class RemoveChavePixController(@Inject private val removeChaveGrpcClient: PixKeymanagerRemoveGrpcServiceGrpc.PixKeymanagerRemoveGrpcServiceBlockingStub) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Delete("/pix/{pixId}")
    fun remove(clientId: UUID, pixId: UUID): HttpResponse<Any> {

        LOGGER.info("$clientId Removendo chave pix com $pixId")
        removeChaveGrpcClient.remove(RemoveChavePixRequest.newBuilder()
            .setClientId(clientId.toString())
            .setPixId(pixId.toString())
            .build())

        return HttpResponse.ok()
    }
}