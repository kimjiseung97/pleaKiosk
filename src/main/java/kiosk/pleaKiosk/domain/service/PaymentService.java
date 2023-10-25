package kiosk.pleaKiosk.domain.service;

import kiosk.pleaKiosk.domain.codes.ErrorCode;
import kiosk.pleaKiosk.domain.codes.SuccessCode;
import kiosk.pleaKiosk.domain.dto.request.PayOneRequest;
import kiosk.pleaKiosk.domain.dto.request.PayRequest;
import kiosk.pleaKiosk.domain.dto.response.ApiResponse;
import kiosk.pleaKiosk.domain.entity.Consumer;
import kiosk.pleaKiosk.domain.entity.Payment;
import kiosk.pleaKiosk.domain.entity.PaymentStatus;
import kiosk.pleaKiosk.domain.repository.ConsumerRepository;
import kiosk.pleaKiosk.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final ConsumerRepository consumerRepository;
    @Transactional
    public ApiResponse<Payment> payment(PayRequest request) {
        Payment payment = paymentRepository.findById(request.getId()).orElseThrow(() -> new NullPointerException());

        if(payment.getPayTotal()!=request.getPayTotal()){
            throw new RuntimeException(String.valueOf(ErrorCode.IO_ERROR));
        }
        updatePayStatus(payment);

        return new ApiResponse(payment, SuccessCode.UPDATE_SUCCESS.getStatus(),SuccessCode.UPDATE_SUCCESS.getMessage());
    }

    private void updatePayStatus(Payment payment) {
        payment.setPayStatus(PaymentStatus.PAID);
    }

    @Transactional
    public ApiResponse<List<Payment>> allPayment(@Valid PayOneRequest request) {
        Consumer consumer = consumerRepository.findById(request.getConsumerId()).orElseThrow(() -> new NullPointerException());
        List<Payment> allPayment = paymentRepository.findAllByConsumer(consumer);
        updateAllPaymentStatus(request, allPayment);

        return new ApiResponse(allPayment,SuccessCode.UPDATE_SUCCESS.getStatus(),SuccessCode.UPDATE_SUCCESS.getMessage());

    }

    private void updateAllPaymentStatus(PayOneRequest request, List<Payment> allPayment) {
        int totalSum = 0;
        for (Payment payment : allPayment) {
            totalSum += payment.getPayTotal();
        }
        if(request.getPayTotal()!=totalSum){
            throw new RuntimeException(String.valueOf(ErrorCode.UPDATE_ERROR));
        }
        allPayment.forEach(this::updatePayStatus);
    }
}
