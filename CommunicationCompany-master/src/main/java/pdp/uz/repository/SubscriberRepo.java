package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pdp.uz.entity.Subscriber;

import java.util.Optional;

@Repository
public interface SubscriberRepo extends JpaRepository<Subscriber, Long> {

    Optional<Subscriber> findBySimCard_Number_CodeAndSimCard_Number_Number(short parseShort, String substring);
}
