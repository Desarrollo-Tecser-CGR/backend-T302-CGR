package com.cgr.base.application.services.role.service.permission.servicesPemission;

import com.cgr.base.domain.models.entity.EntityPermission;
import com.cgr.base.application.services.role.service.permission.RepositoryPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicesPermission {

        @Autowired
        private RepositoryPermission permission;


    public ResponseEntity<List<EntityPermission>> getPermission() {
        List<EntityPermission> permissions = permission.findAll();
        return new ResponseEntity<>(permissions, HttpStatus.OK);
    }

    public  ResponseEntity<EntityPermission> savePermission(EntityPermission entityPermission){
          EntityPermission save =  permission.save(entityPermission);
        return new ResponseEntity<>(save, HttpStatus.OK);
    }
}
