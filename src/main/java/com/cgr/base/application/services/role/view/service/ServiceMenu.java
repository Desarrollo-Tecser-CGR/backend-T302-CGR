package com.cgr.base.application.services.role.view.service;

import com.cgr.base.application.services.role.view.entity.EntitySubMenu;
import com.cgr.base.application.services.role.view.repositoryView.RepositoryView;
import com.cgr.base.infrastructure.repositories.repositories.user.IUserRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceMenu {

    @Autowired
    RepositoryView repositoryView;
    @Autowired
    IUserRepositoryJpa iUserRepositoryJpa;

    public List<EntitySubMenu> getMenu() {
       return repositoryView.findAll();
    }

}
