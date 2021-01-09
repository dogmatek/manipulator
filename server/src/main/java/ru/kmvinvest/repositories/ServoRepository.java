package ru.kmvinvest.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.kmvinvest.entities.ServoEntity;

import java.util.List;


public interface ServoRepository extends CrudRepository<ServoEntity, Integer> {

    @Query("SELECT s from servo s where s.pid =:pid order by s.ordering asc")
    List<ServoEntity> findProgram(@Param("pid") Integer pid);

    @Query("SELECT s from servo s where s.pid =:pid and s.state=1 order by s.ordering asc")
    List<ServoEntity> runProgram(@Param("pid") Integer pid);

    @Query("update servo s set s.state=1 where s.id =:id")
    void published(@Param("id") Integer id);

    @Query("update servo s set s.state=0 where s.id =:id")
    void unpublished(@Param("id") Integer id);


}
