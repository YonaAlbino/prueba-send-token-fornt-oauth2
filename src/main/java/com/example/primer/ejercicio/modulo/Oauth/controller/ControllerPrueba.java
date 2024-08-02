package com.example.primer.ejercicio.modulo.Oauth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerPrueba {

    @GetMapping("/decirHola")
    @PreAuthorize("permitAll()")
    public String saludar(){
        return "Hola, no soy seguro >:D";
    }

    @GetMapping("/logueoOauth2")
    @PreAuthorize("isAuthenticated()")
    public String saludarSec() {
        return "Hola, tengo mucha seguirdad papu!!";
    }
}
