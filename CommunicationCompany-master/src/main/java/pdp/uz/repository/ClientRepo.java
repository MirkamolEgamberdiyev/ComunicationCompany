package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pdp.uz.entity.Client;

import java.util.Optional;

@Repository
public interface ClientRepo extends JpaRepository<Client, Long> {

    Optional<Client> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<Client> findByPassportSeriesAndPassportNumber(String passportSeries, String passportNumber);

    boolean existsByUsernameAndIdNot(String login, Long id);
}
