package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pdp.uz.entity.Activity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityRepo extends JpaRepository<Activity, Long> {

    List<Activity> findAllBySubscriberIdAndCreatedAtBetween(Long id, LocalDateTime start, LocalDateTime end);
}
