package br.com.orangetalent05.pixkeymaneger.shared

import br.com.orangetalent05.PixKeymanagerGrpcServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClientFactory(@GrpcChannel("keyManager") val chanel: ManagedChannel) {

    @Singleton
    fun registraChave() = PixKeymanagerGrpcServiceGrpc.newBlockingStub(chanel)
}