package kiosk.pleaKiosk.domain.controller;
import kiosk.pleaKiosk.domain.dto.request.*;
import kiosk.pleaKiosk.domain.dto.response.OrderResponse;
import kiosk.pleaKiosk.domain.entity.OrderStatus;
import kiosk.pleaKiosk.domain.exception.CustomException;
import kiosk.pleaKiosk.domain.exception.ErrorCode;
import kiosk.pleaKiosk.domain.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
@Slf4j
public class OrderController {

    private final OrderService orderService;
    @PostMapping("/register")
    public ResponseEntity<String> saveOrder(@Valid @RequestBody OrderRequest orderRequest) {
        orderService.registerOrder(orderRequest);
        return ResponseEntity.ok().body("주문 성공");
    }

    @GetMapping("/{productId}/getall-orders")
    public Page<OrderResponse> getOrderListByProduct(@PathVariable Long productId ,@RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "id") String sort){
        if (page < 1 || size < 1) {
            throw new CustomException(ErrorCode.BAD_REQUEST_PAGE);
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sort));
        return orderService.getAllOrderList(productId, pageable);
    }

    @GetMapping("/{consumerId}/myorder")
    public Page<OrderResponse> getMyOrderList(@PathVariable Long consumerId,@RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "id") String sort){
        if (page < 1 || size < 1) {
            throw new CustomException(ErrorCode.BAD_REQUEST_PAGE);
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sort));
        return orderService.getMyOrderList(consumerId,pageable);
    }

    @RequestMapping(value = "/modifymyorder",method = {RequestMethod.PUT ,RequestMethod.PATCH})
    public ResponseEntity<String> modifyOrder(@Valid @RequestBody OrderModifyRequest orderModifyRequest){
        orderService.modifyMyOrder(orderModifyRequest);
        return ResponseEntity.ok().body("주문정보 수정 완료");
    }

    @DeleteMapping("/deleteorder")
    public ResponseEntity<String> deleteProduct(@Valid @RequestBody OrderDeleteRequest orderDeleteRequest){
        orderService.deleteOrder(orderDeleteRequest);
        return ResponseEntity.ok().body(orderDeleteRequest.getId() + "번 주문 삭제완료");
    }

    @RequestMapping(value = "/confirm",method = {RequestMethod.PUT ,RequestMethod.PATCH})
    public ResponseEntity<String> modifyOrder(@Valid @RequestBody OrderJudgeRequest orderJudgeRequest){
        OrderStatus orderStatus = orderService.confirmOrder(orderJudgeRequest);
        return ResponseEntity.ok().body("주문 승인 / 거절 완료");
    }



}
