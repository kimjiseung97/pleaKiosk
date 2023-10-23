package kiosk.pleaKiosk.domain.repository;

import kiosk.pleaKiosk.domain.entity.Consumer;
import kiosk.pleaKiosk.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByOrderId(Long OrderId);

    List<Payment> findAllByConsumer(Consumer consumer);
}
