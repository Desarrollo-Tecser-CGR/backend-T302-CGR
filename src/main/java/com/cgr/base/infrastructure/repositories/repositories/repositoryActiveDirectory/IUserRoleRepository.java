package com.cgr.base.infrastructure.repositories.repositories.repositoryActiveDirectory;

import java.util.List;
import java.util.Optional;


import com.cgr.base.domain.dto.dtoUser.UserDto;
import com.cgr.base.domain.dto.dtoUser.UserWithRolesRequestDto;
import com.cgr.base.domain.models.entity.UserEntity;

public interface IUserRoleRepository {

    public abstract List<UserEntity> findAll();

     public Optional<UserEntity> findByUserId(Long id);

    public abstract UserEntity assignRolesToUser(UserWithRolesRequestDto requestDto);

    public abstract UserDto createUser(UserDto userRequestDto);

    public abstract UserDto updateUser(Long id, UserDto userDto);

}
