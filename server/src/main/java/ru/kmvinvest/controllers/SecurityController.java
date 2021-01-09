package ru.kmvinvest.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

    @GetMapping("/")
    public String hello() {
        return "Добро пожаловать! Для запросов необходимо использовать следующие разделы /servo и /program ";
    }
}
