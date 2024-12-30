package com.test.testactivedirectory.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "SupportTypes")
public class SupportType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int supportTypeId;

    @Column(nullable = false, length = 250)
    private String description;

    // Getters and Setters
}
