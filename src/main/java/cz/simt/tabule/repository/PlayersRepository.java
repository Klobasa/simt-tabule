package cz.simt.tabule.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cz.simt.tabule.data.Player;

@Repository
@Deprecated
public interface PlayersRepository extends CrudRepository<Player, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Player SET checked = 'N'")
    public void uncheckPlayers();
}
