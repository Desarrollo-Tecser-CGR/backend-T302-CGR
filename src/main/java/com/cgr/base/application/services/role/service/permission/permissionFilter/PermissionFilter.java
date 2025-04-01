package com.cgr.base.application.services.role.service.permission.permissionFilter;

import com.cgr.base.application.services.role.service.permission.RepositoryPermission;
import com.cgr.base.application.services.role.service.permission.configPermission.ConfigPermission;
import com.cgr.base.application.services.role.service.permission.servicesPemission.ServicesConfigPermission;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class PermissionFilter {
    @Autowired
    private ServicesConfigPermission servicesConfigPermission;
    @Autowired
    private RepositoryPermission repositoryPermission;

    @Around("@annotation(configPermission)")
    public Object verificarPermiso(ProceedingJoinPoint joinPoint, ConfigPermission configPermission) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        System.out.println(" [Aspect] Entrando en @ConfigPermission");
        System.out.println(" Usuario autenticado: " + (authentication != null ? authentication.getName() : "N/A"));
        System.out.println(" Roles del usuario: " + authentication.getAuthorities());

        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("❌ Usuario no autenticado, bloqueando acceso.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Usuario no autenticado");
        }

        try {
            //  Obtener el nombre de usuario
            String username = authentication.getName();
            System.out.println(" Usuario autenticado: " + username);

            //  Buscar ID del usuario en la base de datos
            Long usuarioId = repositoryPermission.findUserIdByUsername(username);
            if (usuarioId == null) {
                System.out.println("❌ No se encontró el ID del usuario en la BD.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Usuario no encontrado en la base de datos");
            }

            // ✅ Obtener los permisos del usuario usando el servicio
            List<String> permisosUsuario = servicesConfigPermission.obtenerTodosLosPermisosUsuario(usuarioId);
            System.out.println(" Permisos del usuario: " + permisosUsuario);

            //  Verificar si el usuario tiene el permiso requerido (insensible a mayúsculas/minúsculas)
            String permisoRequerido = configPermission.value();
            System.out.println(" Permiso requerido: " + permisoRequerido);

            boolean tienePermiso = permisosUsuario.stream()
                    .anyMatch(permiso -> permiso.equalsIgnoreCase(permisoRequerido));
            System.out.println(" ¿Tiene permiso? " + tienePermiso);

            if (tienePermiso) {
                System.out.println("✅ Permiso concedido, ejecutando método.");
                return joinPoint.proceed();
            } else {
                System.out.println("❌ Permiso denegado, bloqueando acceso.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Permiso denegado");
            }
        } catch (Exception e) {
            System.out.println("❌ Error al verificar permisos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al verificar permisos");
        }
    }
}