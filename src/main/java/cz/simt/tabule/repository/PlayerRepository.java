package cz.simt.tabule.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import cz.simt.tabule.data.Player;

public interface PlayerRepository extends CrudRepository<Player, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Player SET checked = 'N'")
    public void uncheckPlayers();

    @Transactional
    @Query("SELECT p FROM Player p WHERE p.id = ?1")
    Optional<Player> findById(String playerId);

    @Transactional
    @Modifying
    @Query("SELECT p FROM Player p WHERE p.checked = 'N'")
    List<Player> findUnchecked();

    @Transactional
    @Query("SELECT p.id FROM Player p")
    List<String> findAllPlayersId();

    List<Player> findAll();
}
