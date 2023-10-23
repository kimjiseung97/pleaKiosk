package kiosk.pleaKiosk.domain.repository;

import kiosk.pleaKiosk.domain.entity.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumerRepository extends JpaRepository<Consumer,Long> {
}
