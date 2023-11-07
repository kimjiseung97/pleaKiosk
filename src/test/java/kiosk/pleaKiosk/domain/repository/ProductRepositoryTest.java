package kiosk.pleaKiosk.domain.repository;

import kiosk.pleaKiosk.domain.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
class ProductRepositoryTest {

    private final ProductRepository productRepository;

    @Autowired
    ProductRepositoryTest(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Test
    @DisplayName("상품들을 20번 저장한다")
    public void saveProduct(){
        for (int i = 1; i <=20 ; i++) {
            Product build = Product
                    .builder()
                    .amount(150 + i * 10)
                    .name("상품" + i)
                    .price(4000 + i*100)
                    .build();

            Product save = productRepository.save(build);
        }

        List<Product> all = productRepository.findAll();

        assertTrue("all 사이즈는 20이다",all.size()==20);
    }

}