package kiosk.pleaKiosk.domain.repository;
import kiosk.pleaKiosk.domain.entity.Consumer;
import kiosk.pleaKiosk.domain.entity.Order;
import kiosk.pleaKiosk.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
     Page<Order> findByProduct(Product product, Pageable pageable);

     List<Order> findByProduct(Product product);

     Page<Order> findByConsumer(Consumer consumer, Pageable pageable);

}
