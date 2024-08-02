package com.example.primer.ejercicio.modulo.Oauth.Service;


import com.example.primer.ejercicio.modulo.Oauth.dto.AuthLoguinRequestDTO;
import com.example.primer.ejercicio.modulo.Oauth.dto.AuthLoguinResponseDTO;
import com.example.primer.ejercicio.modulo.Oauth.model.UserSec;
import com.example.primer.ejercicio.modulo.Oauth.repository.IUserSecRepository;
import com.example.primer.ejercicio.modulo.Oauth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private IUserSecRepository repo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //Obtener usuario y transformarlo en un objeto UserDetails
        UserSec userSec = repo.findUserEntityByUserName(username).orElseThrow(
                () ->new UsernameNotFoundException("El usuarion " + username +" no fue encontrado"));

        //Lista de permisos
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        //Traer roles y convertilos en SimpleGrantedAuthority
        userSec.getRolesList().stream()
                        .forEach(roles -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(roles.getRole()))));


        //Traer permisos y convertilos en SimpleGrantedAuthority
        userSec.getRolesList().stream()
                .flatMap(role -> role.getPermissionsList().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getPermissionName())));


        return new User(userSec.getUserName(),
                userSec.getPassword(),
                userSec.isEnable(),
                userSec.isAccountNotExpired(),
                userSec.isAccountNotLocked(),
                userSec.isCredentialNotExpired(),
                authorityList);
    }


    public AuthLoguinResponseDTO loguin(AuthLoguinRequestDTO authLoguinRequestDTO) {
        //Se recupera nombre de usario y contraseña
        String username = authLoguinRequestDTO.username();
        String password = authLoguinRequestDTO.password();

        //valida nombre de usuario y contraseña
        Authentication authentication = this.authenticate(username, password);

        //Se guadra la authentication
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //se crea el token

        String accesToken = jwtUtil.createToken(authentication);
        AuthLoguinResponseDTO authLoguinResponseDTO = new AuthLoguinResponseDTO(username, "Loguin correcto", accesToken, true);
        return authLoguinResponseDTO;
    }

    public Authentication authenticate(String username, String password){
        UserDetails userDetails = this.loadUserByUsername(username);

        if(userDetails == null) {
            throw new BadCredentialsException("Nombre de usuario incorrecto");
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Contraseña incorrecta");
        }
        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }

    public Authentication authenticate(String username){
        UserDetails userDetails = this.loadUserByUsername(username);

        if(userDetails == null) {
            throw new BadCredentialsException("Nombre de usuario incorrecto");
        }

        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }
}
