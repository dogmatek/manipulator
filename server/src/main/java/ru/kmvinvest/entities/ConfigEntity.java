package ru.kmvinvest.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

// Конфигурация из базы данных
@Data
@Entity(name = "config")
public class ConfigEntity {

    @Id
    private String key;

    @Column
    private String value;

    @Column
    private String description;
}
