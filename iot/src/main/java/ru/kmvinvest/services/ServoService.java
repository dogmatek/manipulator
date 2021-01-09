package ru.kmvinvest.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kmvinvest.dto.ProgramDto;
import ru.kmvinvest.dto.ServoDto;
import ru.kmvinvest.entities.ServoEntity;
import ru.kmvinvest.repositories.ServoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServoService {
    private final ServoRepository servoRepository;
    private final ProgramService programService;
    private final ManipulatorService manipulatorService;
    private final LogService logService;

    // Выполнение программы манипулятора
    public String run(Integer pid) throws InterruptedException {
        ProgramDto programDto = programService.getById(pid);
        if (programDto.getId() == null) return "Программа с id=" + pid + "отсутствует";
        List<ServoDto> servoDtos = servoRepository.runProgram(pid).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        if (servoDtos.size() == 0) {
            return "Нет активных действий в программе \"" + programDto.getName() + "\" pid=" + pid;
        }
        logService.addAndSout("Готов к запуску программы \"" + programDto.getName() + "\" (" + pid + ")");

        //Запуск манипулятора
        manipulatorService.run(servoDtos);
        return "Программа \"" + programDto.getName() + "\" (" + pid + ") завершена! ";
    }

    // Управление сервоприводом
    public String rotation(Integer pin, Integer angle) {
        //Запуск манипулятора
        return manipulatorService.rotation(pin, angle);
    }

    private ServoEntity mapToEntity(ServoDto dto) {
        ServoEntity entity = new ServoEntity();
        entity.setId(dto.getId());
        entity.setPid(dto.getPid());
        entity.setOrdering(dto.getOrdering());
        entity.setState(dto.getState());
        entity.setPeriod(dto.getPeriod());
        entity.setPin00(dto.getPin00());
        entity.setPin01(dto.getPin01());
        entity.setPin02(dto.getPin02());
        entity.setPin03(dto.getPin03());
        entity.setPin04(dto.getPin04());
        entity.setPin05(dto.getPin05());
        return entity;
    }


    private ServoDto mapToDto(ServoEntity servoEntity) {
        ServoDto dto = new ServoDto();
        dto.setId(servoEntity.getId());
        dto.setPid(servoEntity.getPid());
        dto.setOrdering(servoEntity.getOrdering());
        dto.setState(servoEntity.getState());
        dto.setPeriod(servoEntity.getPeriod());
        dto.setPin00(servoEntity.getPin00());
        dto.setPin01(servoEntity.getPin01());
        dto.setPin02(servoEntity.getPin02());
        dto.setPin03(servoEntity.getPin03());
        dto.setPin04(servoEntity.getPin04());
        dto.setPin05(servoEntity.getPin05());
        return dto;
    }

}
