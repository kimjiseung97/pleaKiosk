package kiosk.pleaKiosk.domain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kiosk.pleaKiosk.domain.dto.request.PayOneRequest;
import kiosk.pleaKiosk.domain.dto.request.PayRequest;
import kiosk.pleaKiosk.domain.dto.response.ErrorResponse;
import kiosk.pleaKiosk.domain.dto.response.commonResponse;
import kiosk.pleaKiosk.domain.dto.response.PaymentResponse;
import kiosk.pleaKiosk.domain.entity.Payment;
import kiosk.pleaKiosk.domain.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kiosk/pay")
@Tag(name = "PAY", description = "결제API")
public class PayController {

    private final PaymentService payService;

    @RequestMapping(value = "/payment",method = {RequestMethod.PUT ,RequestMethod.PATCH})
    @Operation(description = "주문저장 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결제 성공"), @ApiResponse(responseCode = "400", description = "결제 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
    public commonResponse<PaymentResponse> myPayment(@Valid @RequestBody PayRequest request){
        return payService.payment(request);
    }

    @RequestMapping(value = "/allpayment",method = {RequestMethod.PUT ,RequestMethod.PATCH})
    public commonResponse<List<Payment>> allPayment(@Valid @RequestBody PayOneRequest request){
        return payService.allPayment(request);
    }
}
