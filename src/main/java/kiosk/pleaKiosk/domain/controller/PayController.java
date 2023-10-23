package kiosk.pleaKiosk.domain.controller;

import kiosk.pleaKiosk.domain.dto.request.PayOneRequest;
import kiosk.pleaKiosk.domain.dto.request.PayRequest;
import kiosk.pleaKiosk.domain.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pay")
public class PayController {

    private final PaymentService payService;

    @RequestMapping(value = "/payment",method = {RequestMethod.PUT ,RequestMethod.PATCH})
    public ResponseEntity<String> myPayment(@Valid @RequestBody PayRequest request){
        payService.payment(request);
        return ResponseEntity.ok().body("결제완료");
    }

    @RequestMapping(value = "/allpayment",method = {RequestMethod.PUT ,RequestMethod.PATCH})
    public ResponseEntity<String> allPayment(@Valid @RequestBody PayOneRequest request){
        payService.allPayment(request);

        return ResponseEntity.ok().body("일괄 결제 완료");
    }
}
