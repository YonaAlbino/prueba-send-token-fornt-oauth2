package com.example.primer.ejercicio.modulo.Oauth.controller;


import com.example.primer.ejercicio.modulo.Oauth.Service.IRoleService;
import com.example.primer.ejercicio.modulo.Oauth.Service.IUserSecService;
import com.example.primer.ejercicio.modulo.Oauth.model.Role;
import com.example.primer.ejercicio.modulo.Oauth.model.UserSec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
public class UserSecController {

    @Autowired
    private IUserSecService service;

    @Autowired
    private IRoleService roleService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<UserSec>> getAll(){
        List<UserSec> listUser = service.findAll();
        return ResponseEntity.ok(listUser);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<UserSec> findById(@PathVariable Long id){
        Optional<UserSec> userSec = service.findById(id);
        return  userSec.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserSec> save(@RequestBody UserSec userSec){
        Set<Role> roles = new HashSet<>();
        Role roleRead;

        userSec.setPassword(service.encriptPassword(userSec.getPassword()));

        for (Role rol : userSec.getRolesList()){
            roleRead = roleService.findById(rol.getId()).orElse(null);

            if(roleRead != null)
                roles.add(roleRead);
        }

        if(!roles.isEmpty()){
            userSec.setRolesList(roles);
            UserSec userSec1 = service.save(userSec);
            return ResponseEntity.ok(userSec1);
        }

        return null;
    }
}
