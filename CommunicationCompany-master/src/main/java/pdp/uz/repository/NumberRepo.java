package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pdp.uz.entity.Number;

import java.util.Optional;

@Repository
public interface NumberRepo extends JpaRepository<Number, Long> {

    Optional<Number> findByCodeAndNumber(Short code, String number);
}
