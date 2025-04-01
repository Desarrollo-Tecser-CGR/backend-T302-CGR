package com.cgr.base.application.services.role.assignedMenu;

import com.cgr.base.application.services.role.view.entity.EntitySubMenu;
import com.cgr.base.domain.models.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user_submenu")
public class AdinamicMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // <-- Agregar IDENTITY si la clave primaria es autoincremental
    private Integer id_dinamic;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private UserEntity id_user; //

    @ManyToOne
    @JoinColumn(name = "id_submenu", referencedColumnName = "id")
    private EntitySubMenu id_submenu; //
}
