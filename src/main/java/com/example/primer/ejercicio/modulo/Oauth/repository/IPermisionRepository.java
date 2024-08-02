package com.example.primer.ejercicio.modulo.Oauth.repository;


import com.example.primer.ejercicio.modulo.Oauth.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPermisionRepository extends JpaRepository<Permission, Long> {
}
