package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pdp.uz.entity.Package;
import pdp.uz.payload.PackageReport;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackageRepo extends JpaRepository<Package, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    Optional<Package> findByIdAndStatusTrue(Long id);

    @Query(nativeQuery = true, value = "select count(*) as subscribers, t.name as package " +
            "from package t join subscriber_package sp on t.id = sp.packages_id " +
            "group by t.name " +
            "order by count(*) desc ")
    List<PackageReport> getReport();
}