package kiosk.pleaKiosk.domain.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kiosk.pleaKiosk.domain.codes.ErrorCode;
import kiosk.pleaKiosk.domain.dto.request.*;
import kiosk.pleaKiosk.domain.dto.response.*;
import kiosk.pleaKiosk.domain.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kiosk/order")
@Slf4j
@Tag(name = "ORDER", description = "주문API")
public class OrderController {

    private final OrderService orderService;
    @PostMapping("/register")
    @Operation(description = "주문저장 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문등록 성공"), @ApiResponse(responseCode = "400", description = "주문등록 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
    public commonResponse<OrderRegisterResponse> saveOrder(@Valid @RequestBody OrderRequest orderRequest) {
        return orderService.registerOrder(orderRequest);
    }

    @GetMapping("/{productId}/getall-orders")
    @Operation(description = "상품에 들어온 주문을 페이징해서 가져오는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문리스트조회 성공"), @ApiResponse(responseCode = "400", description = "주문리스트 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
    public commonResponse<ProductAndOrderList> getOrderListByProduct(@PathVariable Long productId , @RequestParam(defaultValue = "1") int page,
                                                                     @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sort){
        if (page < 1 || size < 1) {
            throw new IllegalArgumentException("페이지는 음수값이나 0페이지로 반환할 수 없습니다.");
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sort));
        return orderService.getAllOrderList(productId, pageable);
    }

    @GetMapping("/{consumerId}/myorder")
    @Operation(description = "특정 테이블에서 주문한 주문리스트를 페이징해서 가져오는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문리스트조회 성공"), @ApiResponse(responseCode = "400", description = "주문리스트 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
    public commonResponse<Page<OrderRegisterResponse>> getMyOrderList(@PathVariable Long consumerId, @RequestParam(defaultValue = "1") int page,
                                                                      @RequestParam(defaultValue = "10") int size,
                                                                      @RequestParam(defaultValue = "id") String sort){
        if (page < 1 || size < 1) {
            throw new IllegalArgumentException("페이지는 음수값이나 0페이지로 반환할 수 없습니다.");
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sort));
        return orderService.getMyOrderList(consumerId,pageable);
    }

    @RequestMapping(value = "/modifymyorder",method = {RequestMethod.PUT ,RequestMethod.PATCH})
    @Operation(description = "주문을 수정하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문수정 성공"), @ApiResponse(responseCode = "400", description = "주문수정 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
    public commonResponse<OrderModifyResponse> modifyOrder(@Valid @RequestBody OrderModifyRequest orderModifyRequest){
        return orderService.modifyMyOrder(orderModifyRequest);
    }

    @DeleteMapping("/deleteorder")
    @Operation(description = "주문을 취소(삭제)하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문취소(삭제) 성공"), @ApiResponse(responseCode = "400", description = "주문취소(삭제) 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
    public commonResponse deleteProduct(@Valid @RequestBody OrderDeleteRequest orderDeleteRequest){
        return orderService.deleteOrder(orderDeleteRequest);
    }

    @RequestMapping(value = "/confirm",method = {RequestMethod.PUT ,RequestMethod.PATCH})
    @Operation(description = "주문 승인 or 반려하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문상태변경 성공"), @ApiResponse(responseCode = "400", description = "주문상태변경 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
    public commonResponse<OrderConfirmResponse> modifyOrder(@Valid @RequestBody OrderJudgeRequest orderJudgeRequest){
        return orderService.confirmOrder(orderJudgeRequest);
    }


}
