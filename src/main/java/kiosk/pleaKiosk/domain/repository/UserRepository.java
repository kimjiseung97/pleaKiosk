package kiosk.pleaKiosk.domain.repository;

import kiosk.pleaKiosk.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
