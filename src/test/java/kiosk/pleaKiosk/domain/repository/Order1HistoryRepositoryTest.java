package kiosk.pleaKiosk.domain.repository;

import kiosk.pleaKiosk.domain.entity.Order1;
import kiosk.pleaKiosk.domain.entity.Order1History;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;


@SpringBootTest
class Order1HistoryRepositoryTest {

    private final Order1HistoryRepository order1HistoryRepository;

    private final Order1Repository order1Repository;

    @Autowired
    Order1HistoryRepositoryTest(Order1HistoryRepository order1HistoryRepository, Order1Repository order1Repository) {
        this.order1HistoryRepository = order1HistoryRepository;
        this.order1Repository = order1Repository;
    }

    @Test
    @DisplayName("ORDERHISTORY에 값이 들어가야한다")

    public void insertOrderHistory(){

        Order1 order1 = order1Repository.findById(1L).orElseThrow(() -> new NullPointerException());
        for (int i = 0; i <=200 ; i++) {
            order1HistoryRepository.save(Order1History
                    .builder()
                    .order1(order1)
                    .build());
        }
    }
    @Test
    public void findOrderHistory(){
        Order1 order1 = order1Repository.findById(1L).orElseThrow(() -> new NullPointerException());
        List<Order1History> all = order1HistoryRepository.findAll();

        System.out.println("order1 = " + order1.getOrder1Histories());
    }
    
}