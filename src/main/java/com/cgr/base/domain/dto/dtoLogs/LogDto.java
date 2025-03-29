package com.cgr.base.domain.dto.dtoLogs;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class LogDto {

    private String name_user;

    private String correo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Bogota")
    private Date data_session_start;

    private boolean enable;

    private String userCargo;

    private  String tipe_of_income;
}
