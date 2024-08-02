package com.example.primer.ejercicio.modulo.Oauth.controller;


import com.example.primer.ejercicio.modulo.Oauth.Service.UserDetailsServiceImp;
import com.example.primer.ejercicio.modulo.Oauth.dto.AuthLoguinRequestDTO;
import com.example.primer.ejercicio.modulo.Oauth.dto.AuthLoguinResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;

    @PostMapping("/loguin")
    public ResponseEntity<AuthLoguinResponseDTO> loguin(@RequestBody @Valid AuthLoguinRequestDTO userRequest){
        return new ResponseEntity<>(this.userDetailsServiceImp.loguin(userRequest), HttpStatus.OK);
    }

    @GetMapping("/oauth")
    @PreAuthorize("isAuthenticated()")
    public String registroOauth(){
        return "Hola papurri >=)";
    }

}
