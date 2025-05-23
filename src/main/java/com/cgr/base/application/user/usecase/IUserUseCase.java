package com.cgr.base.application.user.usecase;

import java.util.List;

import com.cgr.base.application.user.dto.UserDto;
import com.cgr.base.application.user.dto.UserWithRolesRequestDto;
import com.cgr.base.application.user.dto.UserWithRolesResponseDto;

public interface IUserUseCase {

    public abstract List<UserWithRolesResponseDto> findAll();

    public abstract UserWithRolesResponseDto assignRolesToUser(UserWithRolesRequestDto requestDto);

    public abstract UserDto createUser(UserDto userRequestDto);

    public abstract UserDto updateUser(Long id, UserDto userDto);


}
