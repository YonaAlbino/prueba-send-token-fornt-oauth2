package com.example.primer.ejercicio.modulo.Oauth.controller;


import com.example.primer.ejercicio.modulo.Oauth.Service.IPermissionService;
import com.example.primer.ejercicio.modulo.Oauth.model.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    @Autowired
    private IPermissionService service;


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<Permission>> getAll(){
        List<Permission> listPermissions = service.findAll();
        return ResponseEntity.ok(listPermissions);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Permission> findById(@PathVariable Long id){
        Optional<Permission> permission = service.findById(id);
        return permission.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Permission> save(@RequestBody Permission permission){
        return ResponseEntity.ok(service.save(permission));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
         service.deleteById(id);
         return ResponseEntity.ok("Permiso eliminado");
    }

}
