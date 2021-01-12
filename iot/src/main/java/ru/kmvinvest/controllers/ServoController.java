package ru.kmvinvest.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kmvinvest.services.LogService;
import ru.kmvinvest.services.ServoService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class ServoController {


    private final ServoService servoService;
    private final LogService logService;


    // Запуск программы манипулятора
    @GetMapping("/run/{pid}")
    public String run(@PathVariable Integer pid) throws InterruptedException {
        logService.add("Получен запрос ServoController.run, pid=" + pid);
        return servoService.run(pid);
    }


    // управление сервоприводами манипулятора
    @GetMapping("/rotation/{pin}/{angle}")
    public String rotation(@PathVariable Integer pin, @PathVariable Integer angle) {
        logService.add("Получен запрос ServoController.rotation, pin=" + pin + ", rotation=" + angle);
        return servoService.rotation(pin, angle);
    }

}
