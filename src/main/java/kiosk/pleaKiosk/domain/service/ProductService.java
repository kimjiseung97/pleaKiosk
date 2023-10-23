package kiosk.pleaKiosk.domain.service;
import kiosk.pleaKiosk.domain.dto.request.ProductModifyRequest;
import kiosk.pleaKiosk.domain.dto.request.ProductSaveRequest;
import kiosk.pleaKiosk.domain.entity.Order;
import kiosk.pleaKiosk.domain.entity.Product;
import kiosk.pleaKiosk.domain.exception.CustomException;
import kiosk.pleaKiosk.domain.exception.ErrorCode;
import kiosk.pleaKiosk.domain.repository.OrderRepository;
import kiosk.pleaKiosk.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    private final OrderRepository orderRepository;

    @Transactional
    public void registerProduct(ProductSaveRequest productSaveRequest) {
        log.info("상품 등록 로직 시작 - productSaveRequest = {}",productSaveRequest);

        Product saveProduct = Product
                .builder()
                .name(productSaveRequest.getName())
                .price(productSaveRequest.getPrice())
                .amount(productSaveRequest.getAmount())
                .build();
        productRepository.save(saveProduct);
        log.info("상품 등록 로직 종료");
    }

    @Transactional(readOnly = true)
    public Page<Product> getAllProduct(Pageable pageable) {
        log.info("모든상품 조회 로직 시작 = {}",pageable);
        Page<Product> allProduct = productRepository.findAll(pageable);
        log.info("모든상품 조회 로직 종료 = {}",allProduct.getSize());
        return allProduct;
    }

    @Transactional
    public void modifyProduct(ProductModifyRequest productModifyRequest) {
        log.info("상품 정보 수정 로직 시작 productModifyRequest = {}",productModifyRequest);

        Product findProductById = productRepository.findById(productModifyRequest.getId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ITEM));

        findOrderListProduct(findProductById);

        findProductById.updateEntity(productModifyRequest.getName(),productModifyRequest.getPrice(),productModifyRequest.getAmount());
        log.info("상품 정보 수정 로직 종료 productModifyRequest = {}",findProductById);

    }

    private void findOrderListProduct(Product findProductById) {
        List<Order> findOrderListByProduct= orderRepository.findByProduct(findProductById);
        if(!findOrderListByProduct.isEmpty()){
            throw new CustomException(ErrorCode.CONFLICT);
        }
    }

    @Transactional
    public void deleteProduct(Long id) {
        log.info("상품삭제 로직 시작  - {}",id);

        Product findProductById = productRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ITEM));

        findOrderListProduct(findProductById);

        productRepository.deleteById(id);

        log.info("상품삭제 로직 종료");
    }
}
