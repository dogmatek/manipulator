package ru.kmvinvest.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.kmvinvest.dto.ServoDto;
import ru.kmvinvest.services.LogService;
import ru.kmvinvest.services.RunnerService;
import ru.kmvinvest.services.ServoService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/servo")
public class ServoController {


    private final ServoService servoService;
    private final RunnerService runnerService;

    private final LogService logService;

    @PostMapping
    public ServoDto save(@RequestBody ServoDto servoDto) {
        logService.add("Вызов метода ServoController.save");
        return servoService.save(servoDto);
    }


    @GetMapping
    public List<ServoDto> findAll() {
        return servoService.getAll();
    }


    @GetMapping("/program/{id}")
    public List<ServoDto> findProgram(@PathVariable Integer id) {
        return servoService.getProgram(id);
    }

    // Запуск программы манипулятора
    @PutMapping("/run/{pid}")
    public String run(@PathVariable Integer pid) {
        return runnerService.run(pid);
    }

    // управление сервоприводами манипулятора
    @PutMapping("/rotation/{pin}/{rotation}")
    public String rotation(@PathVariable Integer pin, @PathVariable Integer rotation) throws Exception {
        return runnerService.rotation(pin, rotation);
    }

    @GetMapping("/{id}")
    public ServoDto findById(@PathVariable Integer id) {
        return servoService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        servoService.delete(id);
    }

}
