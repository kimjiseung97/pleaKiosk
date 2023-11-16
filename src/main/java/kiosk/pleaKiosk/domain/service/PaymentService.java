package kiosk.pleaKiosk.domain.service;

import kiosk.pleaKiosk.domain.codes.SuccessCode;
import kiosk.pleaKiosk.domain.dto.request.PayOneRequest;
import kiosk.pleaKiosk.domain.dto.request.PayRequest;
import kiosk.pleaKiosk.domain.dto.response.commonResponse;
import kiosk.pleaKiosk.domain.dto.response.PaymentResponse;
import kiosk.pleaKiosk.domain.entity.Consumer;
import kiosk.pleaKiosk.domain.entity.Payment;
import kiosk.pleaKiosk.domain.entity.PaymentStatus;
import kiosk.pleaKiosk.domain.repository.ConsumerRepository;
import kiosk.pleaKiosk.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final ConsumerRepository consumerRepository;
    @Transactional
    public commonResponse<PaymentResponse> payment(PayRequest request) {
        Payment payment = paymentRepository.findById(request.getId()).orElseThrow(() -> new NullPointerException("해당번호의 결제정보를 찾을 수 없습니다"));
        log.info("결제 완료 로직 시작 = {}",payment);

        if(payment.getPayStatus()==PaymentStatus.PAID){
            throw new RuntimeException("이미 결제완료된 결제 정보 입니다");
        }

        if(payment.getPayTotal()!=request.getPayTotal()){
            throw new RuntimeException("금액이 모자릅니다");
        }
        updatePayStatus(payment);

        payment.getOrder().setPaymentStatus(PaymentStatus.PAID);

        PaymentResponse paymentResponse = makePaymentRsponse(payment);
        log.info("결제 완료 로직 종료");

        return new commonResponse<>(paymentResponse,SuccessCode.UPDATE_SUCCESS.getStatus(),SuccessCode.UPDATE_SUCCESS.getMessage());
    }

    private PaymentResponse makePaymentRsponse(Payment payment) {
        return PaymentResponse
                .builder()
                .paymentStatus(payment.getPayStatus())
                .payTotal(payment.getPayTotal())
                .paymentId(payment.getId())
                .OrderId(payment.getOrder().getId())
                .consumerId(payment.getConsumer().getId())
                .payDateTime(payment.getCreatedDate())
                .paymentId(payment.getId())
                .build();
    }

    private void updatePayStatus(Payment payment) {
        payment.setPayStatus(PaymentStatus.PAID);
        payment.setCreatedDate(LocalDateTime.now());
    }

    @Transactional
    public commonResponse<List<Payment>> allPayment(@Valid PayOneRequest request) {
        Consumer consumer = consumerRepository.findById(request.getConsumerId()).orElseThrow(() -> new NullPointerException("해당 고객은 존재하지않습니다"));
        List<Payment> allPayment = paymentRepository.findAllByConsumer(consumer);
        updateAllPaymentStatus(request, allPayment);

        return new commonResponse<>(allPayment,SuccessCode.UPDATE_SUCCESS.getStatus(),SuccessCode.UPDATE_SUCCESS.getMessage());

    }

    private void updateAllPaymentStatus(PayOneRequest request, List<Payment> allPayment) {
        int totalSum = 0;
        for (Payment payment : allPayment) {
            totalSum += payment.getPayTotal();
        }
        if(request.getPayTotal()!=totalSum){
            throw new RuntimeException("금액이 모자릅니다");
        }
        allPayment.forEach(this::updatePayStatus);
    }
}
