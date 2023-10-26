package kiosk.pleaKiosk.domain.controller;
import kiosk.pleaKiosk.domain.dto.request.*;
import kiosk.pleaKiosk.domain.dto.response.*;
import kiosk.pleaKiosk.domain.entity.Order;
import kiosk.pleaKiosk.domain.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
@Slf4j
public class OrderController {

    private final OrderService orderService;
    @PostMapping("/register")
    public ApiResponse<Order> saveOrder(@Valid @RequestBody OrderRequest orderRequest) {
        return orderService.registerOrder(orderRequest);
    }

    @GetMapping("/{productId}/getall-orders")
    public ApiResponse<ProductAndOrderList> getOrderListByProduct(@PathVariable Long productId , @RequestParam(defaultValue = "1") int page,
                                                                  @RequestParam(defaultValue = "10") int size,
                                                                  @RequestParam(defaultValue = "id") String sort) throws IOException {
        if (page < 1 || size < 1) {
            throw new IllegalArgumentException("페이지는 음수값이나 0페이지로 반환할 수 없습니다.");
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sort));
        return orderService.getAllOrderList(productId, pageable);
    }

    @GetMapping("/{consumerId}/myorder")
    public ApiResponse<Page<OrderRegisterResponse>> getMyOrderList(@PathVariable Long consumerId, @RequestParam(defaultValue = "1") int page,
                                                                   @RequestParam(defaultValue = "10") int size,
                                                                   @RequestParam(defaultValue = "id") String sort) throws IOException {
        if (page < 1 || size < 1) {
            throw new IllegalArgumentException("페이지는 음수값이나 0페이지로 반환할 수 없습니다.");
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sort));
        return orderService.getMyOrderList(consumerId,pageable);
    }

    @RequestMapping(value = "/modifymyorder",method = {RequestMethod.PUT ,RequestMethod.PATCH})
    public ApiResponse<OrderModifyResponse> modifyOrder(@Valid @RequestBody OrderModifyRequest orderModifyRequest){
        return orderService.modifyMyOrder(orderModifyRequest);
    }

    @DeleteMapping("/deleteorder")
    public ApiResponse deleteProduct(@Valid @RequestBody OrderDeleteRequest orderDeleteRequest){
        return orderService.deleteOrder(orderDeleteRequest);
    }

    @RequestMapping(value = "/confirm",method = {RequestMethod.PUT ,RequestMethod.PATCH})
    public ApiResponse modifyOrder(@Valid @RequestBody OrderJudgeRequest orderJudgeRequest){
        return orderService.confirmOrder(orderJudgeRequest);
    }


}
