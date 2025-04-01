package com.cgr.base.application.services.role.assignedMenu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping
@RestController("api/v1/menuDinamic")
public class ControllerDinamic {
    @Autowired
    private ServiceDinamicMenu serviceDinamicMenu;
    @PostMapping("/create")
    public ResponseEntity<AdinamicMenu> create(@RequestBody AdinamicMenu adinamicMenu) {
        return serviceDinamicMenu.createDinamic(adinamicMenu);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AdinamicMenu>> all() {
        return ResponseEntity.ok(serviceDinamicMenu.all());
    }
}
