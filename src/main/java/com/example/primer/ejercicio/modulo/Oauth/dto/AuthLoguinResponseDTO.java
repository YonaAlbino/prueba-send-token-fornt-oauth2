package com.example.primer.ejercicio.modulo.Oauth.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "username", "message","jwt", "status"
})
public record AuthLoguinResponseDTO(String username,
                                    String message,
                                    String jwt,
                                    boolean status) {
}
