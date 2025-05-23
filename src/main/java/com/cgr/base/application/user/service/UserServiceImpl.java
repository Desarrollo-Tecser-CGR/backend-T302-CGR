package com.cgr.base.application.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cgr.base.application.logs.dto.LogDto;
import com.cgr.base.application.user.dto.UserDto;
import com.cgr.base.application.user.dto.UserWithRolesRequestDto;
import com.cgr.base.application.user.dto.UserWithRolesResponseDto;
import com.cgr.base.application.user.usecase.IUserUseCase;
import com.cgr.base.domain.repository.ILogRepository;
import com.cgr.base.domain.repository.IRoleRepository;
import com.cgr.base.domain.repository.IUserRoleRepository;
import com.cgr.base.infrastructure.persistence.entity.LogEntity;
import com.cgr.base.infrastructure.persistence.entity.UserEntity;
import com.cgr.base.infrastructure.utilities.DtoMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserUseCase {

    private final IUserRoleRepository userRoleRepository;

    private final ILogRepository logRepository;

    private final DtoMapper dtoMapper;

    @Transactional(readOnly = true)
    @Override
    public List<UserWithRolesResponseDto> findAll() {
        List<UserWithRolesResponseDto> users = new ArrayList<>();
        this.userRoleRepository.findAll().forEach(user -> {
            var userResponsive = new UserWithRolesResponseDto();
            userResponsive.setIdUser(user.getId());
            userResponsive.setUserName(user.getSAMAccountName());
            userResponsive.setFullName(user.getFullName());
            userResponsive.setEmail(user.getEmail());
            userResponsive.setPhone(user.getPhone());
            userResponsive.setEnabled(user.getEnabled());
            userResponsive.setDateModify(user.getDateModify());
            userResponsive.setCargo(user.getCargo());
            userResponsive.setUserType(user.getUserType());

            List<LogEntity> logs = this.logRepository.findLogByUserEntityId(user.getId());
            List<LogDto> logDtos = this.dtoMapper.convertToListDto(logs, LogDto.class);

            userResponsive.setLogs(logDtos);
            userResponsive.addRole(user.getRoles());

            users.add(userResponsive);
        });
        return users;
    }

    @Transactional
    @Override
    public UserWithRolesResponseDto assignRolesToUser(UserWithRolesRequestDto requestDto) {
        UserEntity userEntity = this.userRoleRepository.assignRolesToUser(requestDto);
        var userResponsive = new UserWithRolesResponseDto();
        userResponsive.setIdUser(userEntity.getId());
        userResponsive.setUserName(userEntity.getSAMAccountName());
        userResponsive.setFullName(userEntity.getFullName());
        userResponsive.setEmail(userEntity.getEmail());
        userResponsive.setPhone(userEntity.getPhone());
        userResponsive.setEnabled(userEntity.getEnabled());
        userResponsive.setDateModify(userEntity.getDateModify());
        userResponsive.setCargo(userEntity.getCargo());
        userResponsive.addRole(userEntity.getRoles());
        return userResponsive;
    }

    @Transactional
    @Override
    public UserDto createUser(UserDto userRequestDto) {

        UserDto user = this.userRoleRepository.createUser(userRequestDto);

        if (user != null) {
            return user;
        }
        return null;

    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        UserDto user = this.userRoleRepository.updateUser(id, userDto);

        if (user != null) {
            return user;
        }
        return null;
    }
}