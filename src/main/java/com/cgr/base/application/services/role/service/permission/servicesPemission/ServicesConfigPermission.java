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
            // 🔍 Obtener el nombre de usuario
            String username = authentication.getName();
            System.out.println("🔍 Usuario autenticado: " + username);

            // 🔍 Buscar ID del usuario en la base de datos
            Long usuarioId = repositoryPermission.findUserIdByUsername(username);
            if (usuarioId == null) {
                System.out.println("❌ No se encontró el ID del usuario en la BD.");
                return false;
            }

            // 🔍 Obtener permisos del usuario
            List<String> permisosUsuario = repositoryPermission.findPermisosByUserId(usuarioId);
            System.out.println("🔍 Permisos del usuario: " + permisosUsuario);

            // 🔍 Verificar si tiene el permiso requerido
            return permisosUsuario != null && permisosUsuario.contains(nombrePermiso);
        } catch (Exception e) {
            System.out.println("❌ Error al verificar permisos: " + e.getMessage());
            return false;
        }
    }

    public List<String> obtenerTodosLosPermisosUsuario(Long usuarioId) {
        return repositoryPermission.findPermisosByUserId(usuarioId);
    }
}
