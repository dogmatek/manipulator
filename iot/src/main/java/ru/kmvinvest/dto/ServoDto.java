package ru.kmvinvest.dto;

import lombok.Data;
import lombok.Setter;

@Setter
@Data
public class ServoDto {
    private Integer id;

    private Integer pid;    // ID программы

    private Integer ordering;

    private Integer state;

    private Integer period;

    private Integer pin00;

    private Integer pin01;

    private Integer pin02;

    private Integer pin03;

    private Integer pin04;

    private Integer pin05;
}