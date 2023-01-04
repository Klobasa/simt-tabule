package cz.simt.tabule.repository;

import cz.simt.tabule.data.PlayersList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayersListRepository extends CrudRepository<PlayersList, Long> {

    Optional<PlayersList> findFirstByNick(String nick);
}
