package cz.simt.tabule.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import cz.simt.tabule.data.GroupStation;


public interface GroupStationRepository extends CrudRepository<GroupStation, Integer> {

    Iterable<GroupStation> findAllByOrderByNameAsc();

    @Transactional
    @Query("SELECT COUNT(s) FROM GroupStation s WHERE LOWER(s.name) = ?1")
    int countByName(String name);

    @Transactional
    @Query("SELECT s.id FROM GroupStation s WHERE LOWER(s.name) = ?1")
    int findIdByName(String name);

    @Transactional
    @Query("SELECT s.ids FROM GroupStation s WHERE s.urlName = ?1")
    String findIdsByUrlName(String urlName);

    @Transactional
    @Query("SELECT s FROM GroupStation s WHERE s.ids LIKE ?1 OR s.ids LIKE ?2 OR s.ids LIKE ?3")
    GroupStation findNameByStationId(String id1, String id2, String id3);

    GroupStation findFirstByUrlName(String url);

    GroupStation findFirstById(int id);
}
