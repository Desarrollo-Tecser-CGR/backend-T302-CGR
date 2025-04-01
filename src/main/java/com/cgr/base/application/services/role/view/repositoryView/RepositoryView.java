package com.cgr.base.application.services.role.view.repositoryView;

import com.cgr.base.application.services.role.view.entity.EntitySubMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryView extends JpaRepository<EntitySubMenu, Integer> {

   List<EntitySubMenu> getMenu();

   void getMenuID (User id);


}
