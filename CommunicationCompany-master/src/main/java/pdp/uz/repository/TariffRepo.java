package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pdp.uz.entity.Tariff;

import java.util.List;
import java.util.Optional;

@Repository
public interface TariffRepo extends JpaRepository<Tariff, Long> {

    Optional<Tariff> findByIdAndActiveTrue(Long tariffId);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    @Query(nativeQuery = true, value = "select count(*) as subscribers, t.name as tariff\n" +
            "from tariff t join subscriber s on t.id = s.tariff_id and s.status = true\n" +
            "group by t.name\n" +
            "order by count(*) desc")
    List<TariffRepo> getReport();

    List<Tariff> findAllByActiveTrue();
}
