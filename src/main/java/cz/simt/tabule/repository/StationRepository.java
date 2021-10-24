package cz.simt.tabule.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cz.simt.tabule.data.Station;

@Repository
public interface StationRepository extends CrudRepository<Station, Long> {



    @Transactional
    @Query("SELECT s FROM Station s WHERE s.gameId = ?1 AND s.stationTraction = ?2")
    Optional<Station> findByGameIdAndTraction(Long gameId, int traction);

}
