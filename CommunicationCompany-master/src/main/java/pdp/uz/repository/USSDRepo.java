package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pdp.uz.entity.USSDCodes;

import java.util.List;
import java.util.Optional;

@Repository
public interface USSDRepo extends JpaRepository<USSDCodes, Long> {

    Optional<USSDCodes> findByCodeAndActiveTrue(String code);

    boolean existsByCode(String code);

    List<USSDCodes> findAllByActiveTrue();

    boolean existsByCodeAndIdNot(String code, Long id);

    Optional<USSDCodes> findByIdAndActiveTrue(Long id);
}
