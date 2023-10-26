package kiosk.pleaKiosk.domain.service;
import kiosk.pleaKiosk.domain.codes.ErrorCode;
import kiosk.pleaKiosk.domain.codes.SuccessCode;
import kiosk.pleaKiosk.domain.dto.request.OrderDeleteRequest;
import kiosk.pleaKiosk.domain.dto.request.OrderJudgeRequest;
import kiosk.pleaKiosk.domain.dto.request.OrderModifyRequest;
import kiosk.pleaKiosk.domain.dto.request.OrderRequest;
import kiosk.pleaKiosk.domain.dto.response.*;
import kiosk.pleaKiosk.domain.entity.*;
import kiosk.pleaKiosk.domain.repository.ConsumerRepository;
import kiosk.pleaKiosk.domain.repository.OrderRepository;
import kiosk.pleaKiosk.domain.repository.PaymentRepository;
import kiosk.pleaKiosk.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ConsumerRepository consumerRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public ApiResponse<Order> registerOrder(OrderRequest orderRequest) {
        log.info("주문등록 로직 시작 ={}",orderRequest);

        Product product = productRepository.findById(orderRequest.getProductId()).orElseThrow(() ->new NullPointerException("해당하는 상품을 찾을 수 없습니다"));
        Consumer consumer = consumerRepository.findById(orderRequest.getTableId()).orElseThrow(() -> new NullPointerException("해당하는 테이블을 찾을 수 없습니다"));

        Order order = saveProductAndOrderAndPayment(orderRequest, product, consumer);
        log.info("주문등록 로직 종료");
        return new ApiResponse(order,SuccessCode.INSERT_SUCCESS.getStatus(),SuccessCode.INSERT_SUCCESS.getMessage());

    }

    private Order saveProductAndOrderAndPayment(OrderRequest orderRequest, Product product, Consumer consumer) {
        int amount = product.getAmount();

        if (amount<orderRequest.getAmount()){
            throw new RuntimeException(String.valueOf(ErrorCode.INSERT_ERROR));
        }

        if(amount==0){
            Order newOrder = getOrder(orderRequest, product, consumer,OrderStatus.PENDING);
            orderRepository.save(newOrder);
        }


        Order newOrder = getOrder(orderRequest, product, consumer,OrderStatus.PENDING);
        Order save = orderRepository.save(newOrder);
        product.setAmount(amount- orderRequest.getAmount());
        return save;
    }

    private Payment getPayment(Consumer consumer, Order newOrder, int price) {
        return Payment.builder()
                .order(newOrder)
                .consumer(consumer)
                .payStatus(PaymentStatus.UNPDAID)
                .payTotal(newOrder.getAmount() * price)
                .build();
    }

    private Order getOrder(OrderRequest orderRequest, Product product, Consumer consumer, OrderStatus orderStatus) {
        return Order
                    .builder()
                    .orderStatus(orderStatus)
                    .consumer(consumer)
                    .amount(orderRequest.getAmount())
                    .product(product)
                    .build();
    }

    @Transactional(readOnly = true)
    public ApiResponse<ProductAndOrderList> getAllOrderList(Long productId, Pageable pageable) {
        log.info("전체 주문리스트 페이징 로직 시작 = {}",pageable);
        Product product = productRepository.findById(productId).orElseThrow(() -> new NullPointerException("해당하는 상품이 존재하지않습니다"));
        Page<Order> allOrderList = orderRepository.findByProduct(product, pageable);
        return makeProductAndOrderList(product, allOrderList);
    }

    private ApiResponse<ProductAndOrderList> makeProductAndOrderList(Product product, Page<Order> allOrderList) {
        ProductResponse productResponse = ProductResponse
                .builder()
                .amount(product.getAmount())
                .productName(product.getName())
                .id(product.getId())
                .build();

        Page<OrderList> orderList = allOrderList.map(Order -> OrderList
                .builder()
                .amount(Order.getAmount())
                .orderStatus(Order.getOrderStatus())
                .consumerId(Order.getConsumer().getId())
                .orderId(Order.getId())
                .createdDate(Order.getCreatedDate())
                .lastModifiedDate(Order.getLastModifiedDate())
                .build());

        ProductAndOrderList productAndOrderList = ProductAndOrderList
                .builder()
                .orderList(orderList)
                .productResponse(productResponse)
                .build();

        return new ApiResponse(productAndOrderList,SuccessCode.SELECT_SUCCESS.getStatus(),SuccessCode.SELECT_SUCCESS.getMessage());
    }

    private ApiResponse<Page<OrderRegisterResponse>> getPageApiResponse(Page<Order> allOrderList) {
        Page<OrderRegisterResponse> orderRegisterResponses = allOrderList.map(Order -> OrderRegisterResponse
                .builder()
                .orderId(Order.getId())
                .productId(Order.getProduct().getId())
                .productName(Order.getProduct().getName())
                .amount(Order.getAmount())
                .orderId(Order.getId())
                .consumerId(Order.getConsumer().getId())
                .createdDate(Order.getCreatedDate())
                .orderStatus(Order.getOrderStatus())
                .build());

        return new ApiResponse(orderRegisterResponses, SuccessCode.SELECT_SUCCESS.getStatus(),SuccessCode.SELECT_SUCCESS.getMessage());
    }

    @Transactional(readOnly = true)
    public ApiResponse<Page<OrderRegisterResponse>> getMyOrderList(Long consumerId, Pageable pageable) {
        log.info("내 주문리스트 확인 로직 시작= {}",pageable);
        Consumer consumer = consumerRepository.findById(consumerId).orElseThrow(() -> new NullPointerException("해당하는 고객이 존재하지않습니다"));
        Page<Order> findOrderListByConsumer = orderRepository.findByConsumer(consumer, pageable);
        log.info("내 주문리스트 확인 로직 종료");

        return getPageApiResponse(findOrderListByConsumer);

    }

    @Transactional
    public ApiResponse<OrderModifyResponse> modifyMyOrder(OrderModifyRequest orderModifyRequest) {
        log.info("주문 수정 로직 시작 = {}",orderModifyRequest);
        //주문번호로 주문 가져오기
        Order findOrderById = orderRepository.findById(orderModifyRequest.getId()).orElseThrow(() -> new NullPointerException(orderModifyRequest.getId()+ "번 주문은 존재하지않습니다"));

        //주문번호에 주문된 상품
        Product findProductByOrderNumber = findOrderById.getProduct();
        updateOrderAndProductAmount(orderModifyRequest, findOrderById, findProductByOrderNumber);

        log.info("주문수정 로직 종료");
        OrderModifyResponse orderModifyResponse = makeModifyOrder(findOrderById);

        return new ApiResponse(orderModifyResponse,SuccessCode.UPDATE_SUCCESS.getStatus(), SuccessCode.UPDATE_SUCCESS.getMessage());
    }


    private OrderModifyResponse makeModifyOrder(Order findOrderById) {
        return OrderModifyResponse
                .builder()
                .id(findOrderById.getId())
                .consumerId(findOrderById.getConsumer().getId())
                .productId(findOrderById.getProduct().getId())
                .productName(findOrderById.getProduct().getName())
                .amount(findOrderById.getAmount())
                .createdDate(findOrderById.getCreatedDate())
                .lastModifiedDate(findOrderById.getLastModifiedDate())
                .orderStatus(findOrderById.getOrderStatus())
                .build();
    }

    private void updateOrderAndProductAmount(OrderModifyRequest orderModifyRequest, Order findOrderById, Product findProductByOrderNumber) throws RuntimeException {
        //주문상태가 승인이거나 거절이라면 익셉션 처리
        if(findOrderById.getOrderStatus()==OrderStatus.APPROVED || findOrderById.getOrderStatus()==OrderStatus.REJECTED){
            throw new RuntimeException(String.valueOf(ErrorCode.UPDATE_ERROR));
        }
        //상품번호가 바뀌었다면
        if(!Objects.equals(findProductByOrderNumber.getId(), orderModifyRequest.getProductId())) {
            modifyAmountAndProduct(orderModifyRequest, findOrderById, findProductByOrderNumber);
        }

        modifyOrderAmount(orderModifyRequest, findOrderById, findProductByOrderNumber);
    }

    private void modifyOrderAmount(OrderModifyRequest orderModifyRequest, Order findOrderById, Product findProductByOrderNumber) {

        //원래 주문 수량
        int originalOrderedAmount = findOrderById.getAmount();
        //수정요청 주문수량
        int updateRequestAmount = orderModifyRequest.getAmount();
        //제품 남은재고
        int leftamount = findProductByOrderNumber.getAmount();

        //주문수정요청이 원래 주문수량보다많으면서 재고수량보단 적어야됌
        if(updateRequestAmount>leftamount){
            throw new RuntimeException("주문수량이 재고보다많습니다");
        }
        
        if(originalOrderedAmount < updateRequestAmount){
            findProductByOrderNumber.setAmount(leftamount - (updateRequestAmount - originalOrderedAmount));
            findOrderById.setAmount(originalOrderedAmount + (updateRequestAmount-originalOrderedAmount));
        }
        findOrderById.setAmount(originalOrderedAmount - (originalOrderedAmount-updateRequestAmount));
        findOrderById.setLastModifiedDate(LocalDateTime.now());
        findProductByOrderNumber.setAmount(leftamount + (originalOrderedAmount-updateRequestAmount));

    }

    private void modifyAmountAndProduct(OrderModifyRequest orderModifyRequest, Order findOrderById, Product findProductByOrderNumber) {
            //원래 주문 주문수정요청대로 업데이트해준다
            Product modifyOrderProduct = productRepository.findById(orderModifyRequest.getProductId()).orElseThrow(() -> new NullPointerException(orderModifyRequest.getProductId() + "번 상품은 존재하지않습니다"));
            //재고보다 주문요청이 크면 안됀다
            if(modifyOrderProduct.getAmount() < orderModifyRequest.getAmount()){
                throw new RuntimeException("재고보다 주문수량이 많습니다");
            }
            findOrderById.setProduct(modifyOrderProduct);
            findOrderById.setAmount(orderModifyRequest.getAmount());
            findOrderById.setLastModifiedDate(LocalDateTime.now());
            modifyOrderProduct.setAmount(modifyOrderProduct.getAmount() - orderModifyRequest.getAmount());
            //원래 들어와있던 주문의 상품재고도 다시 업데이트 해줘야됌
            findProductByOrderNumber.setAmount(findProductByOrderNumber.getAmount() + findOrderById.getAmount());
    }

    @Transactional
    public ApiResponse deleteOrder(OrderDeleteRequest orderDeleteRequest) {
        log.info("주문삭제 정보 로직 시작 - {}",orderDeleteRequest.getId());
        List<Order> orders = orderRepository.findAllById(Collections.singleton(orderDeleteRequest.getId()));

        List<Order> completedOrders = orders.stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.APPROVED)
                .collect(Collectors.toList());
        if(!completedOrders.isEmpty()){
            throw new RuntimeException("해당주문은 이미 승인되었습니다");
        }
        updateProductAmount(orderDeleteRequest);

        orderRepository.deleteById(orderDeleteRequest.getId());

        log.info("주문삭제 정보 로직 종료");

        return new ApiResponse(SuccessCode.DELETE_SUCCESS.getStatus(),SuccessCode.DELETE_SUCCESS.getMessage());
    }

    private void updateProductAmount(OrderDeleteRequest orderDeleteRequest) {
        Order order = orderRepository.findById(orderDeleteRequest.getId()).orElseThrow(() -> new NullPointerException(orderDeleteRequest.getId() +"번 주문은 존재하지않습니다"));
        Product product = order.getProduct();
        product.setAmount(product.getAmount() + order.getAmount());
    }

    @Transactional
    public ApiResponse confirmOrder(OrderJudgeRequest orderJudgeRequest) {
        log.info("주문 승인로직 시작 = {}",orderJudgeRequest);
        Order order = orderRepository.findById(orderJudgeRequest.getId()).orElseThrow(() -> new NullPointerException(orderJudgeRequest.getId() + "번 주문은 존재하지않습니다"));
        confirmOrder(orderJudgeRequest, order);
        log.info("주문 승인로직 종료");
        return new ApiResponse(SuccessCode.UPDATE_SUCCESS.getStatus(),SuccessCode.UPDATE_SUCCESS.getMessage());
    }

    private void confirmOrder(OrderJudgeRequest orderJudgeRequest, Order order) {

        Payment byOrderId = paymentRepository.findByOrderId(orderJudgeRequest.getId());
        if(byOrderId!=null){
            throw new NullPointerException("해당 결제는 존재하지않습니다");
        }

        updateOrderStatus(orderJudgeRequest, order);
        if(orderJudgeRequest.getOrderStatus().equals(OrderStatus.APPROVED)){
            Product product = order.getProduct();
            Consumer consumer = order.getConsumer();
            Payment payment = getPayment(consumer, order,product.getPrice());
            paymentRepository.save(payment);
        }
    }

    private void updateOrderStatus(OrderJudgeRequest orderJudgeRequest, Order order) {
        order.setOrderStatus(orderJudgeRequest.getOrderStatus());
    }
}
