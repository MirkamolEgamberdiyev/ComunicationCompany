package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pdp.uz.entity.Number;
import pdp.uz.entity.SimCard;
import pdp.uz.payload.SimCardReport;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SimCardRepo extends JpaRepository<SimCard, Long> {

    SimCard findByNumber(Number number);

    List<SimCard> findAllByStatusTrue();

    Optional<SimCard> findByIdAndStatusTrue(Long id);


    @Query(nativeQuery = true, value = "select count(*) as soldSimCard, b.name from sim_card t\n" +
            "join branch b on b.id = t.branch_id\n" +
            "group by b.name order by count(*) desc")
    List<SimCardReport> getReport();

    @Query(nativeQuery = true, value = "select sum(t.price)\n" +
            "from sim_card t\n" +
            "where t.created_at between :date1 and :date2")
    Double getIncome(LocalDate date1, LocalDate date2);
}
