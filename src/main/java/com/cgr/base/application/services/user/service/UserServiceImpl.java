package com.cgr.base.application.services.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.cgr.base.domain.models.entity.Logs.RoleEntity;
import com.cgr.base.infrastructure.repositories.repositories.role.IRoleRepositoryJpa;
import com.cgr.base.infrastructure.repositories.repositories.user.IUserRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cgr.base.domain.dto.dtoLogs.LogDto;
import com.cgr.base.domain.dto.dtoUser.UserDto;
import com.cgr.base.domain.dto.dtoUser.UserWithRolesRequestDto;
import com.cgr.base.domain.dto.dtoUser.UserWithRolesResponseDto;
import com.cgr.base.application.services.user.usecase.IUserUseCase;
import com.cgr.base.infrastructure.repositories.repositories.repositoryActiveDirectory.ILogRepository;
import com.cgr.base.infrastructure.repositories.repositories.repositoryActiveDirectory.IUserRoleRepository;
import com.cgr.base.domain.models.entity.Logs.LogEntity;
import com.cgr.base.domain.models.entity.UserEntity;
import com.cgr.base.infrastructure.utilities.DtoMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserUseCase {

    private final IUserRoleRepository userRoleRepository;

    private final ILogRepository logRepository;

    private final DtoMapper dtoMapper;

   // agregado por jhon repositorio
    @Autowired
    private IRoleRepositoryJpa roleRepositoryJpa;
    @Autowired
    private IUserRepositoryJpa iUserRepositoryJpa;

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
        // Buscar el usuario por ID
        Optional<UserEntity> optionalUser = userRoleRepository.findByUserId(requestDto.getIdUser());

        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("El usuario con ID " + requestDto.getIdUser() + " no existe.");
        }

        UserEntity userEntity = optionalUser.get();

        // Buscar los roles en la BD
        List<RoleEntity> roles = roleRepositoryJpa.findByIdIn(requestDto.getRoleIds());

        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron los roles proporcionados.");
        }

        // Busqueda del rol administrador
        boolean addingAdminRole = roles.stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("Administrador"));

        if (addingAdminRole && hasThreeAdminUsers()) {
            throw new IllegalStateException("Ya se encuentran registrados los tres administradores");
        }

        // Asignar los nuevos roles al usuario
        userEntity.getRoles().addAll(roles);

        // Guardar los cambios en la BD
        userEntity = iUserRepositoryJpa.save(userEntity);

        // Crear la respuesta
        var userResponse = new UserWithRolesResponseDto();
        userResponse.setIdUser(userEntity.getId());
        userResponse.setUserName(userEntity.getSAMAccountName());
        userResponse.setFullName(userEntity.getFullName());
        userResponse.setEmail(userEntity.getEmail());
        userResponse.setPhone(userEntity.getPhone());
        userResponse.setEnabled(userEntity.getEnabled());
        userResponse.setDateModify(userEntity.getDateModify());
        userResponse.setCargo(userEntity.getCargo());
        userResponse.addRole(userEntity.getRoles());

        return userResponse;
    }
    // busqueda del rol administrador
    @Transactional(readOnly = true)
     private boolean hasThreeAdminUsers() {
         return userRoleRepository.findAll().stream()
                 .filter(user -> user.getRoles().stream()
                         .anyMatch(role -> role.getName().equalsIgnoreCase("Administrador")))
                 .count() >= 3;
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