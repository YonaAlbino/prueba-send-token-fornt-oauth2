package com.example.primer.ejercicio.modulo.Oauth.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthLoguinRequestDTO(@NotBlank String username,
                                   @NotBlank String password) {

}
