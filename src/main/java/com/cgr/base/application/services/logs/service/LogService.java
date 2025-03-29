package com.cgr.base.application.services.logs.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.cgr.base.domain.dto.dtoEntityProvitionalPlan.EntityProvitionalPlanDto;
import com.cgr.base.domain.models.entity.EntityProvitionalPlan;
import com.cgr.base.infrastructure.repositories.repositories.logs.ILogsRepositoryJpa;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cgr.base.domain.dto.dtoAuth.AuthRequestDto;
import com.cgr.base.domain.dto.dtoLogs.LogDto;
import com.cgr.base.application.services.logs.usecase.ILogUseCase;
import com.cgr.base.infrastructure.repositories.repositories.repositoryActiveDirectory.ILogRepository;

import com.cgr.base.domain.models.entity.Logs.LogEntity;
import com.cgr.base.infrastructure.utilities.DtoMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LogService implements ILogUseCase {

    private final ILogRepository adapterLogRepository;

    private final DtoMapper dtoMapper;

    private final ILogsRepositoryJpa iLogsRepositoryJpa;


    @Override
    @Transactional
    public List<LogDto> logFindAll() {
        List<LogDto> logsDto = this.dtoMapper.convertToListDto(this.adapterLogRepository.logFindAll(), LogDto.class);
        return logsDto;
    }

    @Override
    public LogEntity createLog(AuthRequestDto userRequest) {
        LogEntity logEntity = new LogEntity(userRequest.getEmail(), new Date(), true, userRequest.getSAMAccountName(),userRequest.getTipe_of_income());
        return this.adapterLogRepository.createLog(logEntity, userRequest.getSAMAccountName());
    }


    public Map<String, Long> contarIntentosPorTipo() {
        List<LogEntity> todosLosLogs = iLogsRepositoryJpa.findAll();

        return todosLosLogs.stream()
                .filter(log -> log.getTipe_of_income() != null) // Filtra registros con tipe_of_income nulo
                .collect(Collectors.groupingBy(LogEntity::getTipe_of_income, Collectors.counting()));
    }


}
