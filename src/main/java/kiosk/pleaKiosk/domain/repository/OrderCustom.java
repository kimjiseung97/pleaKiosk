package kiosk.pleaKiosk.domain.repository;

import kiosk.pleaKiosk.domain.entity.Order;
import kiosk.pleaKiosk.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderCustom {
    Page<Order> getAllOrderList(Product product, Pageable pageable);
}
