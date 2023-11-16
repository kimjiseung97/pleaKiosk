package kiosk.pleaKiosk.domain.repository;

import kiosk.pleaKiosk.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCustom {
    Page<Product> findAllProduct(Pageable pageable);
}
