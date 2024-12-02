package com.test.testactivedirectory.infrastructure.persistence.repository.tables;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.test.testactivedirectory.infrastructure.persistence.entity.Tables.InfGeneral;

public interface InfoGeneralRepository extends JpaRepository<InfGeneral, Integer> {

    @Query("SELECT ig FROM InfGeneral ig WHERE ig.codigo LIKE '1%' AND ig.codigo IS NOT NULL")
    List<InfGeneral> findInfGeneralCumpleRuleOne();

    @Query("SELECT ig FROM InfGeneral ig WHERE ig.codigo NOT LIKE '1%' OR ig.codigo IS NULL")
    List<InfGeneral> findInfGeneralNotCumpleRuleOne();

}