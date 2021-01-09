package ru.kmvinvest.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kmvinvest.entities.ConfigEntity;
import ru.kmvinvest.repositories.ConfigRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ConfigService {
    private final ConfigRepository configRepository;

    private final Map<String, String> configs = new HashMap<>();


    public String getValue(String key, String defaultValue) {
        if (configs.isEmpty()) updateConfig();
        return (configs.get(key) != null) ? configs.get(key) : defaultValue;
    }

    // Обновление конфигурации из базы данных
    public void updateConfig() {
        List<ConfigEntity> configEntities = StreamSupport.stream(configRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        for (ConfigEntity configEntity : configEntities) {
            configs.put(configEntity.getKey(), configEntity.getValue());
        }
    }
}
