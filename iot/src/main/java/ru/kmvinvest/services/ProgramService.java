package ru.kmvinvest.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kmvinvest.dto.ProgramDto;
import ru.kmvinvest.entities.ProgramEntity;
import ru.kmvinvest.repositories.ProgramRepository;

@Service
@RequiredArgsConstructor
public class ProgramService {
    private final ProgramRepository programRepository;
    private final LogService logService;

    // Вернуть ProgramDto по id
    public ProgramDto getById(Integer id) {
        try {
            return mapToDto(programRepository.findById(id).orElse(new ProgramEntity()));
        } catch (Exception ex) {
            logService.addAndExeption("Ошибка запроса ProgramService.getById: " + ex.getMessage());
        }
        return null;
    }

    private ProgramDto mapToDto(ProgramEntity programEntity) {
        ProgramDto dto = new ProgramDto();
        dto.setId(programEntity.getId());
        dto.setName(programEntity.getName());
        return dto;
    }


}
