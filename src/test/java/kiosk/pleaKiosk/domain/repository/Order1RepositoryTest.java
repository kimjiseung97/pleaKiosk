package kiosk.pleaKiosk.domain.repository;

import kiosk.pleaKiosk.domain.entity.Order1;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class Order1RepositoryTest {
    private final Order1Repository order1Repository;

    @Autowired
    Order1RepositoryTest(Order1Repository order1Repository) {
        this.order1Repository = order1Repository;
    }

    @DisplayName("주문이 들어가야한다")
    @Test
    public void insertOrder(){
        for (int i = 1; i <=200 ; i++) {
            order1Repository.save(Order1
                    .builder()
                    .orderTime(LocalDateTime.now())
                    .build());
        }

        List<Order1> all = order1Repository.findAll();

        assertTrue(all.size()==200);
    }
}