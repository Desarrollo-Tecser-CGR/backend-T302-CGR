package com.cgr.base.application.services.role.assignedMenu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceDinamicMenu {
     @Autowired
    private RepositoryDinamicMenu repositoryDinamicMenu;

    public ResponseEntity<AdinamicMenu> createDinamic(@RequestBody AdinamicMenu adinamicMenu) {
        AdinamicMenu savedMenu = repositoryDinamicMenu.save(adinamicMenu);
        return new ResponseEntity<>(savedMenu, HttpStatus.CREATED);
    }

     public List<AdinamicMenu> all(){
         return repositoryDinamicMenu.findAll();
    }

}
