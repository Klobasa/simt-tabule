package cz.simt.tabule.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cz.simt.tabule.data.Route;

@Repository
public interface RouteRepository extends CrudRepository<Route, Long> {

    @Transactional
    @Query("SELECT r FROM Route r WHERE r.line = ?1 AND r.direction = ?2 ORDER BY r.sequence")
    List<Route> findRoute(String line, String direction);

    @Transactional
    @Query("SELECT r FROM Route r WHERE LENGTH(r.direction) = 1")
    List<Route> findAllRoutesWithoutDepot();

    @Transactional
    @Query(value = "SELECT CASE WHEN EXISTS (SELECT r.line FROM Route r WHERE r.line = ?1 GROUP BY r.line) THEN 1 ELSE 0 END", nativeQuery = true)
    int countRoute(String line);

    @Transactional
    @Query(value = "SELECT r.line from Route r GROUP BY r.line")
    List<String> getAllLines();

    @Transactional
    @Query(value = "SELECT r.direction FROM Route r WHERE r.line=?1 GROUP BY r.line, r.direction")
    List<String> getDirectionForLine(String line);

    @Transactional
    @Query(value = "SELECT r.traction FROM Route r WHERE r.line LIKE ?1 LIMIT 1", nativeQuery = true)
    Optional<Integer> getLineTraction(String line);
} 
