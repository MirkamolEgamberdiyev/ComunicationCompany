package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pdp.uz.entity.ActivityType;

import java.util.Optional;

@Repository
public interface ActivityTypeRepo extends JpaRepository<ActivityType, Long> {

    Optional<ActivityType> findByName(String name);
}
