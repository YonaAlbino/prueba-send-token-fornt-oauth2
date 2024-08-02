package com.example.primer.ejercicio.modulo.Oauth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/saludar")
@PreAuthorize("denyAll()")
public class SaludoController {

    @GetMapping("/holaSeguro")
    @PreAuthorize("hasRole('ADMIN')")
    public String saludoSeguro(){
        return "Hola con seguridad";
    }

    @GetMapping("/holaNoSeguro")
    @PreAuthorize("permitAll()")
    public String saludoNoSeguro(){
        return "Hola sin seguridad";
    }

}
