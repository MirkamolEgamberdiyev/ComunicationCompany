package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pdp.uz.entity.ServiceCategory;
import pdp.uz.entity.enums.ServiceCategoryEnum;

import java.util.Optional;

@Repository
public interface ServiceCategoryRepo extends JpaRepository<ServiceCategory, Long> {

    Optional<ServiceCategory> findByName(ServiceCategoryEnum valueOf);
}
