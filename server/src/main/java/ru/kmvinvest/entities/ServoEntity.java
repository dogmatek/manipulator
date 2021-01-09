package ru.kmvinvest.entities;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity(name = "servo")
public class ServoEntity {
    @Id
    @Column
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    private Integer id;

    @Column
    private Integer pid;    // ID программы
    @Column
    private Integer ordering; // поле для сортировки
    @Column
    private Integer state;
    @Column
    private Integer period;
    @Column
    private Integer pin00;
    @Column
    private Integer pin01;
    @Column
    private Integer pin02;
    @Column
    private Integer pin03;
    @Column
    private Integer pin04;
    @Column
    private Integer pin05;


}



