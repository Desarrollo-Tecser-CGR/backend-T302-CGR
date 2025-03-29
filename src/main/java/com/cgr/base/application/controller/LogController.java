package com.cgr.base.application.controller;

import com.cgr.base.application.services.logs.service.LogService;
import com.cgr.base.application.services.role.service.permission.configPermission.ConfigPermission;
import com.cgr.base.domain.dto.dtoLogs.LogDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "api/v1/logs")
@RestController
public class LogController {
    @Autowired
    private LogService logService;

    @GetMapping("/conteo")
    public ResponseEntity<Map<String, Long>> obtenerConteoIntentos() {
        Map<String, Long> conteoIntentos = logService.contarIntentosPorTipo();
        return ResponseEntity.ok(conteoIntentos);
    }


    @GetMapping("/allLogs")
    @ConfigPermission(value = "leer_productos", userIdParam = "usuarioId")
    public ResponseEntity<List<LogDto>> viewAll() {
        List<LogDto> logs = logService.logFindAll();
        return new ResponseEntity<>(logs, HttpStatus.ACCEPTED);
    }
}
