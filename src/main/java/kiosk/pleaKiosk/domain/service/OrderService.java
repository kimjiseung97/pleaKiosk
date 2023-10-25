package kiosk.pleaKiosk.domain.service;
import kiosk.pleaKiosk.domain.dto.request.OrderDeleteRequest;
import kiosk.pleaKiosk.domain.dto.request.OrderJudgeRequest;
import kiosk.pleaKiosk.domain.dto.request.OrderModifyRequest;
import kiosk.pleaKiosk.domain.dto.request.OrderRequest;
import kiosk.pleaKiosk.domain.dto.response.OrderResponse;
import kiosk.pleaKiosk.domain.entity.*;
import kiosk.pleaKiosk.domain.exception.CustomException;
import kiosk.pleaKiosk.domain.exception.ErrorCode;
import kiosk.pleaKiosk.domain.repository.ConsumerRepository;
import kiosk.pleaKiosk.domain.repository.OrderRepository;
import kiosk.pleaKiosk.domain.repository.PaymentRepository;
import kiosk.pleaKiosk.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void registerOrder(OrderRequest orderRequest) {
        log.info("주문등록 로직 시작 ={}",orderRequest);

        Product product = productRepository.findById(orderRequest.getProductId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ITEM));
        Consumer consumer = consumerRepository.findById(orderRequest.getTableId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ITEM));

        saveProductAndOrderAndPayment(orderRequest, product, consumer);
        log.info("주문등록 로직 종료");
    }

    private void saveProductAndOrderAndPayment(OrderRequest orderRequest, Product product, Consumer consumer) {
        int amount = product.getAmount();

        if (amount<orderRequest.getAmount()){
            throw new CustomException(ErrorCode.UNPROCESSABLE_ENTITY);
        }

        if(amount==0){
            Order newOrder = getOrder(orderRequest, product, consumer,OrderStatus.PENDING);
            orderRepository.save(newOrder);
        }


        Order newOrder = getOrder(orderRequest, product, consumer,OrderStatus.PENDING);
        orderRepository.save(newOrder);
        product.setAmount(amount- orderRequest.getAmount());

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
    public Page<OrderResponse> getAllOrderList(Long productId, Pageable pageable) {
        log.info("전체 주문리스트 페이징 로직 시작 = {}",pageable);
        Product product = productRepository.findById(productId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ITEM));
        Page<Order> allOrderList = orderRepository.findByProduct(product, pageable);
        log.info("전체 주문리스트 페이징 로직 종료");

        return allOrderList.map(Order -> OrderResponse
                .builder()
                .productId(Order.getProduct().getId())
                .productName(Order.getProduct().getName())
                .amount(Order.getAmount())
                .orderId(Order.getId())
                .consumerId(Order.getConsumer().getId())
                .createdDate(Order.getCreatedDate())
                .lastModifiedDate(Order.getLastModifiedDate())
                .orderStatus(Order.getOrderStatus())
                .build());

    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getMyOrderList(Long consumerId, Pageable pageable) {
        log.info("내 주문리스트 확인 로직 시작= {}",pageable);
        Consumer consumer = consumerRepository.findById(consumerId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ITEM));
        Page<Order> findOrderListByConsumer = orderRepository.findByConsumer(consumer, pageable);
        log.info("내 주문리스트 확인 로직 종료");

        return findOrderListByConsumer.map(Order -> OrderResponse
                .builder()
                .productId(Order.getProduct().getId())
                .productName(Order.getProduct().getName())
                .amount(Order.getAmount())
                .orderId(Order.getId())
                .consumerId(Order.getConsumer().getId())
                .createdDate(Order.getCreatedDate())
                .lastModifiedDate(Order.getLastModifiedDate())
                .orderStatus(Order.getOrderStatus())
                .build());
    }

    @Transactional
    public void modifyMyOrder(OrderModifyRequest orderModifyRequest) {
        log.info("주문 수정 로직 시작 = {}",orderModifyRequest);
        //주문번호로 주문 가져오기
        Order findOrderById = orderRepository.findById(orderModifyRequest.getId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ITEM));
        //주문번호에 주문된 상품
        Product findProductByOrderNumber = findOrderById.getProduct();
        updateOrderAndProductAmount(orderModifyRequest, findOrderById, findProductByOrderNumber);
        log.info("주문수정 로직 종료");

    }

    private void updateOrderAndProductAmount(OrderModifyRequest orderModifyRequest, Order findOrderById, Product findProductByOrderNumber) {
        //주문상태가 승인이거나 거절이라면 익셉션 처리
        if(findOrderById.getOrderStatus()==OrderStatus.APPROVED || findOrderById.getOrderStatus()==OrderStatus.REJECTED){
            throw new CustomException(ErrorCode.FORBIDDEN_ORDER);
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
            throw new CustomException(ErrorCode.UNPROCESSABLE_ENTITY);
        }
        
        if(originalOrderedAmount < updateRequestAmount){
            findProductByOrderNumber.setAmount(leftamount - (updateRequestAmount - originalOrderedAmount));
            findOrderById.setAmount(originalOrderedAmount + (updateRequestAmount-originalOrderedAmount));
        }
        findOrderById.setAmount(originalOrderedAmount - (originalOrderedAmount-updateRequestAmount));
        findProductByOrderNumber.setAmount(leftamount + (originalOrderedAmount-updateRequestAmount));
    }

    private void modifyAmountAndProduct(OrderModifyRequest orderModifyRequest, Order findOrderById, Product findProductByOrderNumber) {
            //원래 주문 주문수정요청대로 업데이트해준다
            Product modifyOrderProduct = productRepository.findById(orderModifyRequest.getProductId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ITEM));
            //재고보다 주문요청이 크면 안됀다
            if(modifyOrderProduct.getAmount() < orderModifyRequest.getAmount()){
                throw new CustomException(ErrorCode.UNPROCESSABLE_ENTITY);
            }
            findOrderById.setProduct(modifyOrderProduct);
            findOrderById.setAmount(orderModifyRequest.getAmount());
            modifyOrderProduct.setAmount(modifyOrderProduct.getAmount() - orderModifyRequest.getAmount());
            //원래 들어와있던 주문의 상품재고도 다시 업데이트 해줘야됌
            findProductByOrderNumber.setAmount(findProductByOrderNumber.getAmount() + findOrderById.getAmount());
    }

    @Transactional
    public void deleteOrder(OrderDeleteRequest orderDeleteRequest) {
        log.info("주문삭제 정보 로직 시작 - {}",orderDeleteRequest.getId());
        List<Order> orders = orderRepository.findAllById(Collections.singleton(orderDeleteRequest.getId()));

        List<Order> completedOrders = orders.stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.APPROVED)
                .collect(Collectors.toList());
        if(!completedOrders.isEmpty()){
            throw new CustomException(ErrorCode.FORBIDDEN_ORDER);
        }
        updateProductAmount(orderDeleteRequest);

        orderRepository.deleteById(orderDeleteRequest.getId());

        log.info("주문삭제 정보 로직 종료");
    }

    private void updateProductAmount(OrderDeleteRequest orderDeleteRequest) {
        Order order = orderRepository.findById(orderDeleteRequest.getId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));
        Product product = order.getProduct();
        product.setAmount(product.getAmount() + order.getAmount());
    }

    @Transactional
    public OrderStatus confirmOrder(OrderJudgeRequest orderJudgeRequest) {
        log.info("주문 승인로직 시작 = {}",orderJudgeRequest);
        Order order = orderRepository.findById(orderJudgeRequest.getId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));
        confirmOrder(orderJudgeRequest, order);
        log.info("주문 승인로직 종료");
        return order.getOrderStatus();
    }

    private void confirmOrder(OrderJudgeRequest orderJudgeRequest, Order order) {

        Payment byOrderId = paymentRepository.findByOrderId(orderJudgeRequest.getId());
        if(byOrderId!=null){
            throw new CustomException(ErrorCode.CONFLICT);
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
