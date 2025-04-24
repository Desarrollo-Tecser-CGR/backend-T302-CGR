package com.cgr.base.domain.dto.dtoExport;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class ExportDto {

    String path;

    String name;

    private int year;
    private int month;
    private long count;


    public ExportDto(int year, int month, long count) {
        this.year = year;
        this.month = month;
        this.count = count;
        this.path = null;  // O "" si prefieres una cadena vacía
        this.name = null;  // O "" si prefieres una cadena vacía
    }

}
