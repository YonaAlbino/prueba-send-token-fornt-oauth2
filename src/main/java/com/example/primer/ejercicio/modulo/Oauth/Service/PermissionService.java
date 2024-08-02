package com.example.primer.ejercicio.modulo.Oauth.Service;


import com.example.primer.ejercicio.modulo.Oauth.model.Permission;
import com.example.primer.ejercicio.modulo.Oauth.repository.IPermisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService implements  IPermissionService{

    @Autowired
    private IPermisionRepository repository;

    @Override
    public List<Permission> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Permission> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Permission save(Permission permission) {
        return repository.save(permission);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Permission update(Permission permission) {
        return this.save(permission);
    }


}
