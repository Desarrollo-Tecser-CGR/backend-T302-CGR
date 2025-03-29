package com.cgr.base.application.services.logs.usecase;

import java.util.List;
import java.util.Map;

import com.cgr.base.domain.dto.dtoAuth.AuthRequestDto;
import com.cgr.base.domain.dto.dtoLogs.LogDto;
import com.cgr.base.domain.models.entity.Logs.LogEntity;
import org.springframework.http.ResponseEntity;

public interface ILogUseCase {

    public List<LogDto> logFindAll();

    public LogEntity createLog(AuthRequestDto userRequest);



}
