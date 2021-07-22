package br.com.orangetalent05.pixkeymaneger.dtos

import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull



data class PixIdRequest(
    @field: NotBlank val clientId: String,
    @field: NotBlank val pixId: String,
) {


}
