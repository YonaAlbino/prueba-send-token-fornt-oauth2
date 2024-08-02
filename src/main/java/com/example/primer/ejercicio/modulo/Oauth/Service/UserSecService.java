package com.example.primer.ejercicio.modulo.Oauth.Service;


import com.example.primer.ejercicio.modulo.Oauth.model.Role;
import com.example.primer.ejercicio.modulo.Oauth.model.UserSec;
import com.example.primer.ejercicio.modulo.Oauth.repository.IUserSecRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserSecService implements IUserSecService{

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private IUserSecRepository repo;

    @Autowired
    @Lazy
    private IRoleService roleService;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserSec> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<UserSec> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    @Override
    public UserSec save(UserSec userSec) {
        return repo.save(userSec);
    }

    @Override
    public UserSec update(UserSec userSec) {
        return this.save(userSec);
    }

    @Override
    public String encriptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    @Override
    @Transactional
    public UserSec  saveOauthUser(String email) {
        UserSec  user  = new UserSec();
        Set<Role> roleList = new HashSet<>();
        Role rolPorDefecto;

        rolPorDefecto = roleService.getRol("USER");
        Role managedRole = entityManager.merge(rolPorDefecto);

        roleList.add(managedRole);

        user.setEnable(true);
        user.setAccountNotExpired(true);
        user.setAccountNotLocked(true);
        user.setCredentialNotExpired(true);
        user.setUserName(email);
        user.setPassword(passwordEncoder.encode(generatedPassword()));
        user.setRolesList(roleList);

        return  this.save(user);
    }

    public String generatedPassword(){
        return RandomStringUtils.randomAlphanumeric(8);
    }
}
