package cz.simt.tabule.repository;

import cz.simt.tabule.data.Times;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimesRepository extends CrudRepository<Times, String> {
}
