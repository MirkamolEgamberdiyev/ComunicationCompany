package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pdp.uz.entity.Employee;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {

    Optional<Employee> findByUsernameAndActiveTrue(String username);

    Optional<Employee> findByIdAndActiveTrue(Long id);

    List<Employee> findAllByActiveTrue();

    List<Employee> findByActiveTrueAndBranchId(Long branchId);

    boolean existsByUsernameAndActiveTrue(String username);

    boolean existsByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, Long id);
}
