package com.test.testactivedirectory.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.test.testactivedirectory.application.auth.dto.AuthRequestDto;
import com.test.testactivedirectory.application.auth.service.ValidateService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/verificarToken/email")
public class ValidateTokenController {
    private ValidateService validateService;

    @PostMapping("/verify")
    public ResponseEntity <?> validateEmailToken(@PathVariable String Token){
        return ResponseEntity.ok(validateService.validationToken(Token));
    }
  
    

}