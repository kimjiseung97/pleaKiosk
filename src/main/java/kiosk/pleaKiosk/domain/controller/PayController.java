package kiosk.pleaKiosk.domain.controller;

import kiosk.pleaKiosk.domain.dto.request.PayOneRequest;
import kiosk.pleaKiosk.domain.dto.request.PayRequest;
import kiosk.pleaKiosk.domain.dto.response.ApiResponse;
import kiosk.pleaKiosk.domain.entity.Payment;
import kiosk.pleaKiosk.domain.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pay")
public class PayController {

    private final PaymentService payService;

    @RequestMapping(value = "/payment",method = {RequestMethod.PUT ,RequestMethod.PATCH})
    public ApiResponse<Payment> myPayment(@Valid @RequestBody PayRequest request){
        return payService.payment(request);
    }

    @RequestMapping(value = "/allpayment",method = {RequestMethod.PUT ,RequestMethod.PATCH})
    public ApiResponse<List<Payment>> allPayment(@Valid @RequestBody PayOneRequest request){
        return payService.allPayment(request);
    }
}
