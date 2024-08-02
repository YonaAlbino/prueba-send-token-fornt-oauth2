package com.example.primer.ejercicio.modulo.Oauth.Service;

import com.example.primer.ejercicio.modulo.Oauth.model.OidcUserCustom;
import com.example.primer.ejercicio.modulo.Oauth.model.Role;
import com.example.primer.ejercicio.modulo.Oauth.model.UserSec;
import com.example.primer.ejercicio.modulo.Oauth.repository.IRolRepository;
import com.example.primer.ejercicio.modulo.Oauth.repository.IUserSecRepository;
import com.example.primer.ejercicio.modulo.Oauth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


@Service
public class CustomOidcUserService extends OidcUserService {

    @Autowired
    private IUserSecService usuService;

    @Autowired
    private IUserSecRepository usuRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @Lazy
    private UserDetailsServiceImp userDetailsServiceImp;


    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        String jwtToken;
        String email = oidcUser.getEmail();
        Authentication authentication = null;

        Optional<UserSec> userSec = usuRepo.findUserEntityByUserName(email);

        if(userSec.isEmpty()){
            UserSec usuarioCreado = usuService.saveOauthUser(email);
            authentication = userDetailsServiceImp.authenticate(usuarioCreado.getUserName());
        }else {
            authentication = userDetailsServiceImp.authenticate(userSec.get().getUserName());
        }

        jwtToken = jwtUtil.createToken(authentication);
        return new OidcUserCustom(oidcUser, jwtToken);
    }
}
