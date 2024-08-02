package com.example.primer.ejercicio.modulo.Oauth.controller;


import com.example.primer.ejercicio.modulo.Oauth.Service.IPermissionService;
import com.example.primer.ejercicio.modulo.Oauth.Service.IRoleService;
import com.example.primer.ejercicio.modulo.Oauth.model.Permission;
import com.example.primer.ejercicio.modulo.Oauth.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private IRoleService service;

    @Autowired
    private IPermissionService permissionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<Role>> fetAll(){
        List<Role> roleList = service.findAll();
        return ResponseEntity.ok(roleList);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Role> findById(@PathVariable Long id){
        Optional<Role> role = service.findById(id);
        return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    //@PreAuthorize("hasRole('ADMIN') and hasPermission('CREATE')")
    public ResponseEntity<Role> save(@RequestBody Role role){
    Set<Permission> permissionsList = new HashSet<>();
    Permission permissionRead;

    for (Permission per : role.getPermissionsList()){
        permissionRead = permissionService.findById(per.getId()).orElse(null);

        if(permissionRead != null)
            permissionsList.add(permissionRead);
    }

        role.setPermissionsList(permissionsList);
        Role role1 = service.save(role);
        return ResponseEntity.ok(role1);
    }

    @PatchMapping("/addPermission/{idPermission}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Role> addPermission(@RequestBody Role role, @PathVariable Long idPermission) throws Exception {
        Optional<Permission> permission = permissionService.findById(idPermission);

        if(permission.isPresent()){
            role.getPermissionsList().add(permission.get());
            return ResponseEntity.ok(service.update(role));
        }
        throw new Exception("El permiso no existe");
    }


    @PatchMapping("/deletedPermission/{idPermission}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Role> deletPermission(@RequestBody Role role, @PathVariable Long idPermission) throws Exception {
        Permission permissionDeleted = permissionService.findById(idPermission).orElse(null);
        HashSet<Permission> nuevaLista = new HashSet<>();

        if(permissionDeleted != null){
            for (Permission per : role.getPermissionsList()){
                if(per.getId() != permissionDeleted.getId()){
                    nuevaLista.add(per);
                }
            }
            role.setPermissionsList(nuevaLista);
            return ResponseEntity.ok(service.update(role));
        }

        throw new Exception("El permiso no fue encontrado");
    }
}
