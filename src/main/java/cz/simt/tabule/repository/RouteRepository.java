package cz.simt.tabule.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cz.simt.tabule.data.Player;
import cz.simt.tabule.data.Route;

@Repository
public interface RouteRepository extends CrudRepository<Route, Long> {

    @Transactional
    @Query("SELECT r FROM Route r WHERE r.line = ?1 AND r.direction = ?2 ORDER BY r.sequence")
    List<Route> findRoute(String line, String direction);
}
