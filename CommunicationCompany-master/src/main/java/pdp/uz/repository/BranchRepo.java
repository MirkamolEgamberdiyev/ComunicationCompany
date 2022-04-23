package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pdp.uz.entity.Branch;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchRepo extends JpaRepository<Branch, Long> {

    Optional<Branch> findByIdAndActiveTrue(Long branchId);

    List<Branch> findAllByActiveTrue();

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}
