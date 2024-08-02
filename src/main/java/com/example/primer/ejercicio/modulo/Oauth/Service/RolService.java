package com.example.primer.ejercicio.modulo.Oauth.Service;


import com.example.primer.ejercicio.modulo.Oauth.model.Role;
import com.example.primer.ejercicio.modulo.Oauth.repository.IRolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolService implements IRoleService{

    @Autowired
    private IRolRepository repo;

    @Override
    public List<Role> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Role> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Role save(Role role) {
        return repo.save(role);
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    @Override
    public Role update(Role role) {
        return this.save(role);
    }

    @Override
    public Role getRol(String roleName) {
      return repo.findByrole(roleName);
    }
}
