package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pdp.uz.entity.Payment;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long> {
}
