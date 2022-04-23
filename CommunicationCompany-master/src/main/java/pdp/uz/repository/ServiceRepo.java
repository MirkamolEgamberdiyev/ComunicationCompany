package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pdp.uz.entity.Services;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepo extends JpaRepository<Services, Long> {

    Optional<Services> findByIdAndStatusTrue(Long id);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    @Query(nativeQuery = true, value = "select t.name\n" +
            "from services t\n" +
            "         join subscriber_services ss on t.id = ss.services_id\n" +
            "group by t.name order by count(*) desc ")
    List<String> getTopServices();

    @Query(nativeQuery = true, value = "select t.name\n" +
            "from services t\n" +
            "         join subscriber_services ss on t.id = ss.services_id\n" +
            "group by t.name order by count(*)")
    List<String> getPoorServices();
}
