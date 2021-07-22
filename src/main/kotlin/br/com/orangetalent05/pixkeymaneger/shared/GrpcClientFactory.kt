package br.com.orangetalent05.pixkeymaneger.shared

import br.com.orangetalent05.PixKeymanagerConsultaGrpcServiceGrpc
import br.com.orangetalent05.PixKeymanagerGrpcServiceGrpc
import br.com.orangetalent05.PixKeymanagerListaGrpcServiceGrpc
import br.com.orangetalent05.PixKeymanagerRemoveGrpcServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClientFactory(@GrpcChannel("keyManager") val chanel: ManagedChannel) {

    @Singleton
    fun registraChave() = PixKeymanagerGrpcServiceGrpc.newBlockingStub(chanel)

    @Singleton
    fun removeChave() = PixKeymanagerRemoveGrpcServiceGrpc.newBlockingStub(chanel)

    @Singleton
    fun consultaChave() = PixKeymanagerConsultaGrpcServiceGrpc.newBlockingStub(chanel)

    @Singleton
    fun listaChave() = PixKeymanagerListaGrpcServiceGrpc.newBlockingStub(chanel)
}