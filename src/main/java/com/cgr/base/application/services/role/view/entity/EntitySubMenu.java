package com.cgr.base.application.services.role.view.entity;

import com.cgr.base.domain.models.entity.Menu.Menu;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name = "submenus")
public class EntitySubMenu {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "icon")
  private String icon;

  @Column(name = "link")
  private String link;

  @Column(name = "title")
  private String title;

  @Column(name="type")
  private String type;

  @ManyToOne
  @JoinColumn(name = "menu_id", referencedColumnName = "id")
  private Menu menu;
}
