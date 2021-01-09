package ru.kmvinvest.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.kmvinvest.entities.LogEntity;

public interface LogRepository extends CrudRepository <LogEntity, Integer> {

}