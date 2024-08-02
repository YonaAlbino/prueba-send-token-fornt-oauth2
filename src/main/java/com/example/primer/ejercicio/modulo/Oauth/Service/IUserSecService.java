package com.example.primer.ejercicio.modulo.Oauth.Service;



import com.example.primer.ejercicio.modulo.Oauth.model.UserSec;

import java.util.List;
import java.util.Optional;

public interface IUserSecService {

    public List<UserSec> findAll();
    public Optional<UserSec> findById(Long id);
    public void deleteById (Long id);
    public UserSec save (UserSec userSec);
    public UserSec update(UserSec userSec);
    public String encriptPassword(String password);
    public UserSec saveOauthUser(String email);
}
