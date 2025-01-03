package com.test.testactivedirectory.infrastructure.persistence.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.testactivedirectory.infrastructure.persistence.entity.UserEntity;

@Repository
public interface AuthRepositoryJpa extends JpaRepository<UserEntity,Long> { 

    // List<User> findBySAMAccountName(String sAMAccountName);
    UserEntity findBysAMAccountName(String sAMAccountName);

    
} 
