package kiosk.pleaKiosk.domain.service;
import kiosk.pleaKiosk.domain.codes.SuccessCode;
import kiosk.pleaKiosk.domain.dto.request.ProductModifyRequest;
import kiosk.pleaKiosk.domain.dto.request.ProductSaveRequest;
import kiosk.pleaKiosk.domain.dto.response.commonResponse;
import kiosk.pleaKiosk.domain.entity.Order;
import kiosk.pleaKiosk.domain.entity.Product;
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
    public commonResponse<Product> registerProduct(ProductSaveRequest productSaveRequest) {
        log.info("상품 등록 로직 시작 - productSaveRequest = {}",productSaveRequest);

        Product saveProduct = Product
                .builder()
                .name(productSaveRequest.getName())
                .price(productSaveRequest.getPrice())
                .amount(productSaveRequest.getAmount())
                .build();
        Product savedProduct = productRepository.save(saveProduct);
        log.info("상품 등록 로직 종료");

        return new commonResponse(savedProduct,SuccessCode.INSERT_SUCCESS.getStatus(),SuccessCode.INSERT_SUCCESS.getMessage());
    }

    @Transactional(readOnly = true)
    public commonResponse<Page<Product>> getAllProduct(Pageable pageable) {
        log.info("모든상품 조회 로직 시작 = {}",pageable);
        Page<Product> allProduct = productRepository.findAllProduct(pageable);
        log.info("모든상품 조회 로직 종료 = {}",allProduct.getSize());
        return new commonResponse(allProduct,SuccessCode.SELECT_SUCCESS.getStatus(), SuccessCode.SELECT_SUCCESS.getMessage());
    }

    @Transactional
    public commonResponse<Product> modifyProduct(ProductModifyRequest productModifyRequest) {
        log.info("상품 정보 수정 로직 시작 productModifyRequest = {}",productModifyRequest);

        Product findProductById = productRepository.findById(productModifyRequest.getId()).orElseThrow(() -> new NullPointerException("해당 상품을 찾을 수 없습니다"));

        findOrderListProduct(findProductById);

        findProductById.updateEntity(productModifyRequest.getName(),productModifyRequest.getPrice(),productModifyRequest.getAmount());
        log.info("상품 정보 수정 로직 종료 ");

        return new commonResponse<>(findProductById,SuccessCode.UPDATE_SUCCESS.getStatus(), SuccessCode.UPDATE_SUCCESS.getMessage());

    }

    private void findOrderListProduct(Product findProductById) {
        List<Order> findOrderListByProduct= orderRepository.findByProduct(findProductById);
        if(!findOrderListByProduct.isEmpty()){
            throw new RuntimeException("해당상품에 들어온 주문이 존재합니다");
        }
    }

    @Transactional
    public commonResponse deleteProduct(Long id) {
        log.info("상품삭제 로직 시작  - {}",id);

        Product findProductById = productRepository.findById(id).orElseThrow(() -> new NullPointerException("해당 상품은 존재하지않습니다"));

        findOrderListProduct(findProductById);

        productRepository.deleteById(id);

        log.info("상품삭제 로직 종료");

        return new commonResponse(SuccessCode.DELETE_SUCCESS.getStatus(), SuccessCode.DELETE_SUCCESS.getMessage());
    }
}
