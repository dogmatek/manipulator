package ru.kmvinvest.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.kmvinvest.entities.ConfigEntity;

public interface ConfigRepository extends CrudRepository<ConfigEntity, Integer> {

}
