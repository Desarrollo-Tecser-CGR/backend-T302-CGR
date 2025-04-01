package com.cgr.base.application.services.role.service.permission;

import com.cgr.base.domain.models.entity.EntityPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RepositoryPermission extends JpaRepository<EntityPermission, Long> {

    @Query(value = "SELECT DISTINCT p.name_permission " +
            "FROM users u " +
            "LEFT JOIN users_roles ur ON u.id = ur.user_id " +
            "LEFT JOIN roles r ON ur.role_id = r.id_role " +
            "LEFT JOIN roles_permissions rp ON r.id_role = rp.id_role " +
            "LEFT JOIN permission p ON rp.id_permission = p.id_permission " +
            "LEFT JOIN user_permission up ON u.id = up.id_user " +
            "LEFT JOIN permission p2 ON up.id_permission = p2.id_permission " +
            "WHERE u.id = :usuarioId", nativeQuery = true)
    List<String> findPermisosByUserId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT u.id FROM UserEntity u WHERE u.sAMAccountName = :username")
    Long findUserIdByUsername(@Param("username") String username);



}
