package cz.simt.tabule.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cz.simt.tabule.data.Trip;

@Repository
public interface TripRepository extends CrudRepository<Trip, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Trip SET position = false WHERE playerId = ?1 AND position = true")
    void unsetPosition(String playerId);

    @Transactional
    @Modifying
    @Query("UPDATE Trip SET position = true WHERE playerId = ?1 AND stopId = ?2")
    void setPosition(String playerId, String station);

    @Transactional
    @Modifying
    @Query("DELETE FROM Trip WHERE playerId = ?1")
    void deleteByPlayer(String playerId);

    List<Trip> findByStopIdInOrderByTimeAsc(List<String> Ids);

    @Transactional
    @Query("SELECT t.sequence FROM Trip t WHERE t.playerId = ?1 AND t.position = true")
    Optional<Integer> findSequenceByAPlayerId(String playerId);

    List<Trip> findByPlayerIdEqualsOrderBySequenceAsc(String id);

    //Last stop
    Trip findFirstByPlayerIdOrderBySequenceDesc(String id);

    //First stop
    Trip findFirstByPlayerIdOrderBySequenceAsc(String id);

    //Actual stop
    @Query("SELECT t from Trip t WHERE t.playerId = ?1 AND t.position = true")
    Trip findActualStationByPlayerId(String id);

    @Transactional
    @Query("SELECT t.stopId from Trip t WHERE t.playerId = ?1 AND t.position = true")
    Optional<String> findActualStation(String id);

    @Transactional
    @Query("SELECT t.time from Trip t WHERE t.playerId = ?1 AND t.position = true")
    Optional<LocalDateTime> findActualDeparture(String id);
}
