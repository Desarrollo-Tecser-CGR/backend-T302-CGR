package com.cgr.base.application.services.export.service;

import com.cgr.base.domain.dto.dtoExport.ExportDto;
import com.cgr.base.domain.dto.dtoExport.TotalExportCountDto;
import com.cgr.base.domain.models.entity.EntityNotification;
import com.cgr.base.domain.models.entity.ExportCount;
import com.cgr.base.infrastructure.repositories.repositories.RepositoryExportCount;
import com.cgr.base.infrastructure.repositories.repositories.repositoryNotification.RepositoryNotification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExportService {

    @Autowired
    private RepositoryNotification repositoryNotification;
    @Autowired
    private RepositoryExportCount repositoryExportCount;

    public String generateCSV() {
        StringBuilder csvBuilder = new StringBuilder();

        // Crear encabezados
        String[] headers = { "ID", "Notificación", "Sujeto", "Fecha", "Número de contrato", "Usuario", "Cargo" };
        csvBuilder.append(String.join(",", headers)).append("\n");

        // Obtener datos y llenar las filas
        List<EntityNotification> notifications = repositoryNotification.findAll();
        for (EntityNotification notification : notifications) {
            csvBuilder.append(notification.getId()).append(",");
            csvBuilder.append(notification.getNotification() != null ? notification.getNotification() : "N/A")
                    .append(",");
            csvBuilder.append(notification.getSubject() != null ? notification.getSubject() : "N/A").append(",");
            csvBuilder.append(notification.getDate() != null ? notification.getDate().toString() : "N/A").append(",");
            csvBuilder.append(notification.getNumbercontract() != null ? notification.getNumbercontract() : "N/A")
                    .append(",");
            csvBuilder.append(notification.getUser() != null ? notification.getUser().getFullName() : "N/A")
                    .append(",");
            csvBuilder.append(notification.getUser() != null ? notification.getUser().getCargo() : "N/A").append("\n");
        }

        return csvBuilder.toString();
    }

    public void incrementExportCount() {
        ExportCount exportCount = new ExportCount();
        exportCount.setExportDate(new Date());
        exportCount.setExportCount(1); // Incrementa el conteo en 1
        exportCount.setExportType("CSV");
        repositoryExportCount.save(exportCount);
    }

    public TotalExportCountDto getTotalExportCount() {
        Long totalCount = repositoryExportCount.count();
        return new TotalExportCountDto(totalCount);
    }

    public List<Object[]> getDistinctMonthsAndYears() {
        return repositoryExportCount.findDistinctMonthsAndYears();
    }

    public List<ExportDto> getExportCountsByMonthAndYear() {
        List<Object[]> counts = repositoryExportCount.countExportsByMonthAndYear();
        return counts.stream()
                .map(count -> new ExportDto(
                        ((Number) count[0]).intValue(),
                        ((Number) count[1]).intValue(),
                        ((Number) count[2]).longValue()
                ))
                .collect(Collectors.toList());
    }
     /* public List<ExportDto> getExportCountsByMonthAndYear() {
        List<Object[]> counts = repositoryExportCount.countExportsByMonthAndYear();
        return counts.stream()
                .map(count -> new ExportDto(
                        (int) count[0],
                        (int) count[1],
                        (long) count[2] // Asegúrate de castear correctamente
                ))
                .collect(Collectors.toList());
    }*/

}