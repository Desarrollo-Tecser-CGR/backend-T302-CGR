package com.cgr.base.application.controller;

import com.cgr.base.application.services.auth.usecase.IAuthUseCase;
import com.cgr.base.application.services.logs.service.LogService;
import com.cgr.base.application.services.role.service.permission.configPermission.ConfigPermission;
import com.cgr.base.domain.dto.dtoLogs.LogDto;
import com.cgr.base.infrastructure.repositories.repositories.repositoryActiveDirectory.IActiveDirectoryUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @ConfigPermission(value = "analistaView")
    public ResponseEntity<Map<String, Long>> obtenerConteoIntentos() {
        Map<String, Long> conteoIntentos = logService.contarIntentosPorTipo();
        return ResponseEntity.ok(conteoIntentos);
    }


    @GetMapping("/allLogs")
    @ConfigPermission(value = "adminSuper")
    public ResponseEntity<List<LogDto>> viewAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        System.out.println("Entró al controlador de logs 🚀");
        System.out.println("Usuario autenticado: " + authentication.getName());
        System.out.println("Roles: " + authentication.getAuthorities());

        List<LogDto> logs = logService.logFindAll();
        return new ResponseEntity<>(logs, HttpStatus.ACCEPTED);
    }


}
