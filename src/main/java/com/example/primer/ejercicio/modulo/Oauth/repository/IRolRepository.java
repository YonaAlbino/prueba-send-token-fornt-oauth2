package com.example.primer.ejercicio.modulo.Oauth.repository;


import com.example.primer.ejercicio.modulo.Oauth.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRolRepository extends JpaRepository<Role, Long> {
    public Role findByrole(String roleName);
}
