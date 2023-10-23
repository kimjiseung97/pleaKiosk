package kiosk.pleaKiosk.domain.repository;

import kiosk.pleaKiosk.domain.entity.Consumer;
import kiosk.pleaKiosk.domain.entity.Order;
import kiosk.pleaKiosk.domain.entity.OrderStatus;
import kiosk.pleaKiosk.domain.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
class OrderRepositoryTest {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    
    private final ConsumerRepository consumerRepository;
    @Autowired
    OrderRepositoryTest(OrderRepository orderRepository, ProductRepository productRepository, ConsumerRepository consumerRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.consumerRepository = consumerRepository;
    }

    @Test
    @DisplayName("주문이 들어가야한다")
    public void registerOrder(){

        Optional<Consumer> byId = consumerRepository.findById(1L);
        Consumer consumer = byId.get();
        Optional<Product> productById = productRepository.findById(20L);
        Product product = productById.get();

        Order order = Order.builder().orderStatus(OrderStatus.APPROVED).product(product).amount(3).consumer(consumer).build();
        product.setAmount(product.getAmount()-order.getAmount());
        orderRepository.save(order);

        int amount = product.getAmount();

        assertTrue("amount의 값은 147이다",amount==147);
    }
}