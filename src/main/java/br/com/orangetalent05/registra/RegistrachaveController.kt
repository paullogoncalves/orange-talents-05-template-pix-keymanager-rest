package br.com.orangetalent05.registra

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post

@Controller("/chaves")
class RegistrachaveController {

    @Post
    fun registra(request: RegistraChaveRequest): RegistrachaveResponse {

        val chave =
    }
}