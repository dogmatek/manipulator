package ru.kmvinvest.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.kmvinvest.dto.ProgramDto;
import ru.kmvinvest.services.LogService;
import ru.kmvinvest.services.ProgramService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/program")
public class ProgramController {
    private final ProgramService programService;
    private final LogService logService;

    @PostMapping
    public ProgramDto save(@RequestBody ProgramDto programDto) {
        logService.add("Вызов метода ProgramController.save");
        return programService.save(programDto);
    }

    @GetMapping
    public List<ProgramDto> findAll() {
        logService.add("Вызов метода ProgramController.findAll");
        return programService.getAll();
    }

    @GetMapping("/{id}")
    public ProgramDto findById(@PathVariable Integer id) {
        return programService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        logService.add("Вызов метода ProgramController.delete id=" + id);
        programService.delete(id);
    }
}
