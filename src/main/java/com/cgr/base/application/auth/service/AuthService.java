package com.cgr.base.application.auth.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cgr.base.application.Email.EmailService;
import com.cgr.base.application.auth.dto.AuthRequestDto;
import com.cgr.base.application.auth.dto.AuthResponseDto;
import com.cgr.base.application.auth.dto.UserDto;
import com.cgr.base.application.auth.dto.AuthRequestDto;
import com.cgr.base.application.auth.dto.AuthResponseDto;
import com.cgr.base.application.auth.mapper.AuthMapper;
import com.cgr.base.application.auth.usecase.IAuthUseCase;
import com.cgr.base.domain.models.UserModel;
import com.cgr.base.domain.repository.IActiveDirectoryUserRepository;
import com.cgr.base.domain.repository.IUserRepository;
import com.cgr.base.infrastructure.exception.customException.ResourceNotFoundException;
import com.cgr.base.infrastructure.persistence.entity.Menu.Menu;
import com.cgr.base.infrastructure.persistence.entity.RoleEntity;
import com.cgr.base.infrastructure.persistence.entity.UserEntity;
import com.cgr.base.infrastructure.persistence.repository.user.IUserRepositoryJpa;
import com.cgr.base.infrastructure.security.Jwt.providers.JwtAuthenticationProvider;
import com.cgr.base.infrastructure.utilities.EmailUtility;
import com.cgr.base.infrastructure.persistence.entity.RoleEntity;
import com.cgr.base.infrastructure.persistence.entity.UserEntity;
import com.cgr.base.infrastructure.persistence.entity.Menu.Menu;
import com.cgr.base.infrastructure.persistence.repository.user.IUserRepositoryJpa;
import com.cgr.base.infrastructure.security.Jwt.providers.JwtAuthenticationProvider;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService implements IAuthUseCase {

    private final IUserRepository userRepository;

    private final IUserRepositoryJpa userRepositoryFull;

    private final IActiveDirectoryUserRepository activeDirectoryUserRepository;

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final EmailService emailService;

    @Transactional
    @Override
    public Map<String, Object> signIn(AuthRequestDto userRequest, HttpServletRequest servletRequest)
            throws JsonProcessingException {

        Map<String, Object> response = new HashMap<>();

        try {

            UserModel userModel = userRepository.findBySAMAccountName(userRequest.getSAMAccountName());

            System.err.println("userModel: " + userModel);
            if (userModel != null && userModel.getPassword().equals(userRequest.getPassword())) {

                Optional<UserEntity> userOptional = this.userRepositoryFull
                        .findBySAMAccountName(userRequest.getSAMAccountName());
                AuthResponseDto userDto = AuthMapper.INSTANCE.toAuthResponDto(userModel);

                String token = jwtAuthenticationProvider.createToken(userDto, userOptional.get().getRoles());

                userDto.setToken(token);
                userDto.setIsEnable(true);

                response.put("user", userDto);
                response.put("message", "User authenticated successfully");
                response.put("statusCode", 200);
                response.put("status", "success");
                return response;

            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;

    }

    @Transactional
    @Override
    public Map<String, Object> authWithLDAPActiveDirectory(AuthRequestDto userRequest,
            HttpServletRequest servletRequest)
            throws JsonProcessingException {

        Map<String, Object> response = new HashMap<>();

        try {

            Boolean isAccountValid = activeDirectoryUserRepository.checkAccount(
                    userRequest.getSAMAccountName(),
                    userRequest.getPassword());

            if (isAccountValid) {

                UserEntity user = this.userRepositoryFull
                        .findBySAMAccountNameWithRoles(userRequest.getSAMAccountName()).get();
                AuthResponseDto userRequestDto = AuthMapper.INSTANCE.toAuthResponDto(userRequest);

                userRequestDto.setRoles(user.getRoles().stream().map(RoleEntity::getName).toList());

                String token = jwtAuthenticationProvider.createToken(userRequestDto, user.getRoles());

                List<Menu> menus = this.userRepositoryFull
                        .findMenusByRoleNames(user.getRoles().stream().map(RoleEntity::getName).toList());

                userRequestDto.setMenus(menus);

                userRequestDto.setToken(token);
                userRequestDto.setIsEnable(true);

                userRequest.setEmail(user.getEmail());

                response.put("user", userRequestDto);
                response.put("message", "User authenticated successfully");
                response.put("statusCode", 200);
                response.put("status", "success");
                return response;
            }

        } catch (Exception e) {
            System.err.println("Error en la capa de aplicaciontion en service: " + e.getMessage());
        }
        response.put("message", "User not authenticated");
        return response;
    }

    @Transactional
    @Override
    public Map<String, Object> emailLogin(UserDto userRequest)
            throws JsonProcessingException {

        Map<String, Object> response = new HashMap<>();

        UserEntity userLogin = this.userRepositoryFull.findBySAMAccountNameWithRoles(userRequest.getSAMAccountName())
                .orElseThrow(() -> new ResourceNotFoundException("El usuario " + userRequest.getSAMAccountName() + " no existe"));

        
        try {
            
            AuthResponseDto userToken = new AuthResponseDto ();
            userToken.setIsEnable(true);
            userToken.setSAMAccountName(userRequest.getSAMAccountName());


            String emailToken = jwtAuthenticationProvider.createToken(userToken, userLogin.getRoles());

            this.emailService.sendSimpleEmail(userLogin.getEmail(), "Verificacion de Usuario", EmailUtility.getHtmlContent(emailToken));


        } catch (Exception e) {
            // TODO: handle exception
            System.err.println("Error en la capa de aplicaciontion en service: " + e.getMessage());
        }
        response.put("message", "User not authenticated");
        return response;
    }

}
