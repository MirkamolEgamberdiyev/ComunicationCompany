package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pdp.uz.entity.ResidueOpportunity;

@Repository
public interface ResidueOpportunityRepo extends JpaRepository<ResidueOpportunity, Long> {
}
