package com.cgr.base.application.services.role.service.permission.servicesPemission;

import com.cgr.base.application.services.role.service.permission.RepositoryPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ServicesConfigPermission {

    @Autowired
    private RepositoryPermission repositoryPermission;

    @Autowired
    public ServicesConfigPermission(RepositoryPermission repositoryPermission) {
        this.repositoryPermission = repositoryPermission;
    }

    public boolean usuarioTienePermiso(String nombrePermiso) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false; // Usuario no autenticado
        }

        try {
            Long usuarioId = Long.parseLong(authentication.getName());
            List<String> permisosUsuario = repositoryPermission.findPermisosByUserId(usuarioId);
            return permisosUsuario != null && permisosUsuario.contains(nombrePermiso);
        } catch (NumberFormatException e) {
            // Manejar el caso en que authentication.getName() no sea un número válido
            return false;
        }
    }

    public List<String> obtenerTodosLosPermisosUsuario(Long usuarioId) {
        // Llama al método del repositorio para obtener los permisos
        return repositoryPermission.findPermisosByUserId(usuarioId);
    }



}
