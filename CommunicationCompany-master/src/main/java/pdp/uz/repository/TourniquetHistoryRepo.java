package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pdp.uz.entity.TourniquetHistory;

import java.util.UUID;

@Repository
public interface TourniquetHistoryRepo extends JpaRepository<TourniquetHistory, UUID> {
}
