package com.example.primer.ejercicio.modulo.Oauth.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${PRIVATE_KEY}")
    private String privateKey;

    @Value("${USER_GENERATOR}")
    private String userGenerator;

    //Creacion de tokens
    public String createToken(Authentication authentication){
        //Especifica el algoritmo que se va a utilizar
        Algorithm algorithm = Algorithm.HMAC256(privateKey);

        //devuevle el nombre del  usario autenticado (queda guardado en el context holder)
        String username = authentication.getPrincipal().toString();

        //Este metodo recupera los permisos y los transforma a un String separando cada permiso por una coma
        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        //Se crea el token
        String jwtToken = JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(username)
                .withClaim("authorities", authorities)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + (30 * 60000)))
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithm);
        return jwtToken;
    }

    //Decodificacion y validacion del token
    public DecodedJWT validateToken(String token){
        try {
            //Especifica el algoritmo que se uso y la llave secreta
            Algorithm algorithm = Algorithm.HMAC256(privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.userGenerator)
                    .build();
            //Si todo esta ok devuelve el jwt decodificado
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT;
        } catch (JWTVerificationException jwtVerificationException  ){
            throw new JWTVerificationException("Token invalido");
        }
    }

    //Metodo para obtener el username del token
    public String extractUserName(DecodedJWT decodedJWT){
        return  decodedJWT.getSubject().toString();
    }

    //metodo para obtener solo un claim (atributo)
    public Claim getSpecifClaim(DecodedJWT decodedJWT, String claimName){
       return decodedJWT.getClaim(claimName);
    }


    //metodo para obtener todos los claims (atributos)
    public Map<String, Claim> returnAllClaims(DecodedJWT decodedJWT){
        return decodedJWT.getClaims();
    }
}
