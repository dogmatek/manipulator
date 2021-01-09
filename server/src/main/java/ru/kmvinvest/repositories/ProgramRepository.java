package ru.kmvinvest.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.kmvinvest.entities.ProgramEntity;

public interface ProgramRepository extends CrudRepository<ProgramEntity, Integer> {
}
