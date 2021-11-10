package cz.simt.tabule.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import cz.simt.tabule.data.Trip;

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

    Trip findFirstByPlayerIdOrderBySequenceDesc(String id);
}
