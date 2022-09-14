package cz.simt.tabule.repository;

import cz.simt.tabule.data.Timetable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TimetableRepository extends CrudRepository<Timetable, Long> {

    @Transactional
    @Override
    @Query("SELECT t FROM Timetable t")
    List<Timetable> findAll();


}
