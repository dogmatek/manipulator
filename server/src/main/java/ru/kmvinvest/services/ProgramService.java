package ru.kmvinvest.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kmvinvest.dto.ProgramDto;
import ru.kmvinvest.entities.ProgramEntity;
import ru.kmvinvest.repositories.ProgramRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ProgramService {
    private final ProgramRepository programRepository;
    private final LogService logService;

    // Сохранение программы
    public ProgramDto save(ProgramDto dto) {
        if (dto.getName().isEmpty() || dto.getName().equals("")) {
            logService.addAndExeption("ProgramService.save: name не может быть пустым");
            return new ProgramDto();
        }
        ProgramEntity programEntity = mapToEntity(dto);
        ProgramEntity savedEntity = programRepository.save(programEntity);

        try {
            return mapToDto(savedEntity);
        } catch (Exception ex) {
            logService.addAndExeption("Ошибка запроса ProgramService.save: " + ex.getMessage());
        }
        return null;
    }

    // Вернуть список программ
    public List<ProgramDto> getAll() {
        try {
            return StreamSupport.stream(programRepository.findAll().spliterator(), false)
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            logService.addAndExeption("Ошибка запроса ProgramService.getAll: " + ex.getMessage());
        }
        return null;
    }

    // Вернуть ProgramDto по id
    public ProgramDto getById(Integer id) {
        try {
            return mapToDto(programRepository.findById(id).orElse(new ProgramEntity()));
        } catch (Exception ex) {
            logService.addAndExeption("Ошибка запроса ProgramService.getById: " + ex.getMessage());
        }
        return null;
    }

    // Удаление по id
    public void delete(Integer id) {
        try {
            programRepository.deleteById(id);
        } catch (Exception ex) {
            logService.addAndExeption("Ошибка запроса ProgramService.delete: " + ex.getMessage());
        }
    }

    private ProgramEntity mapToEntity(ProgramDto dto) {
        ProgramEntity entity = new ProgramEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        return entity;
    }


    private ProgramDto mapToDto(ProgramEntity programEntity) {
        ProgramDto dto = new ProgramDto();
        dto.setId(programEntity.getId());
        dto.setName(programEntity.getName());
        return dto;
    }
}
