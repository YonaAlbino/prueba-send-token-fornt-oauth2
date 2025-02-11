package com.example.primer.ejercicio.modulo.Oauth.Service;

import com.example.primer.ejercicio.modulo.Oauth.model.OidcUserCustom;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OidcUserCustom oidcUserCustom = (OidcUserCustom) authentication.getPrincipal();
        //System.out.println(oidcUserCustom.jwtTokenInterno());

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"token\":\"" + oidcUserCustom.jwtTokenInterno() + "\"}");

        response.sendRedirect("/oauth");
    }
}
