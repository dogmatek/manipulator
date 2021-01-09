package ru.kmvinvest.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kmvinvest.dto.ServoDto;
import ru.kmvinvest.entities.ServoEntity;
import ru.kmvinvest.repositories.ServoRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ServoService {

    private final ServoRepository servoRepository;
    private final ProgramService programService;
    private final LogService logService;

    // сохранить команду манипулятора
    public ServoDto save(ServoDto dto) {
        // Валидация
        String validation = "";
        if (dto.getPid() <= 0) validation += "Не указан Pid. \n";
        if (programService.getById(dto.getPid()).getId() == null)
            validation += "Программа с  Pid = " + dto.getPid() + " отсутствует.\n";
        if (dto.getPin00() < 0 || dto.getPin00() > 100) validation += "Pin00 вне диапазона 0 - 100.\n";
        if (dto.getPin01() < 0 || dto.getPin01() > 100) validation += "Pin01 вне диапазона 0 - 100.\n";
        if (dto.getPin02() < 0 || dto.getPin02() > 100) validation += "Pin02 вне диапазона 0 - 100.\n";
        if (dto.getPin03() < 0 || dto.getPin03() > 100) validation += "Pin03 вне диапазона 0 - 100.\n";
        if (dto.getPin04() < 0 || dto.getPin04() > 100) validation += "Pin04 вне диапазона 0 - 100.\n";
        if (dto.getPin05() < 0 || dto.getPin05() > 100) validation += "Pin05 вне диапазона 0 - 100.\n";

        if (!validation.equals("")) {
            logService.addAndExeption("Ошибка валидации ServoService.save: " + validation);
            return new ServoDto();
        }
        ServoEntity servoEntity = mapToEntity(dto);
        try {
            return mapToDto(servoRepository.save(servoEntity));
        } catch (Exception ex) {
            logService.addAndExeption("Ошибка запроса ServoService.save: " + ex.getMessage());
        }
        return null;
    }

    // Вернуть все команды манипулятора
    public List<ServoDto> getAll() {
        try {
            return StreamSupport.stream(servoRepository.findAll().spliterator(), false)
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            logService.addAndExeption("Ошибка запроса ServoService.getAll: " + ex.getMessage());
        }
        return null;
    }

    // Вернуть команды манипулятора по pid (id программы)
    public List<ServoDto> getProgram(Integer pid) {
        try {
            return StreamSupport.stream(servoRepository.findProgram(pid).spliterator(), false)
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            logService.addAndExeption("Ошибка запроса ServoService.getProgram: " + ex.getMessage());
        }
        return null;
    }

    // Вернуть ServoDto по id
    public ServoDto getById(Integer id) {
        try {
            return mapToDto(servoRepository.findById(id).orElse(new ServoEntity()));
        } catch (Exception ex) {
            logService.addAndExeption("Ошибка запроса ServoService.getById: " + ex.getMessage());
        }
        return null;
    }

    // Удаление по id
    public void delete(Integer id) {
        try {
            servoRepository.deleteById(id);
        } catch (Exception ex) {
            logService.addAndExeption("Ошибка запроса ServoService.delete: " + ex.getMessage());
        }
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
