package kiosk.pleaKiosk.domain.service;

import kiosk.pleaKiosk.domain.dto.request.PayOneRequest;
import kiosk.pleaKiosk.domain.dto.request.PayRequest;
import kiosk.pleaKiosk.domain.entity.Consumer;
import kiosk.pleaKiosk.domain.entity.Payment;
import kiosk.pleaKiosk.domain.entity.PaymentStatus;
import kiosk.pleaKiosk.domain.exception.CustomException;
import kiosk.pleaKiosk.domain.exception.ErrorCode;
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
    public void payment(PayRequest request) {
        Payment payment = paymentRepository.findById(request.getId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PAYMENT));

        if(payment.getPayTotal()!=request.getPayTotal()){
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        updatePayStatus(payment);
    }

    private void updatePayStatus(Payment payment) {
        payment.setPayStatus(PaymentStatus.PAID);
    }

    @Transactional
    public void allPayment(@Valid PayOneRequest request) {
        Consumer consumer = consumerRepository.findById(request.getConsumerId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ITEM));
        List<Payment> allPayment = paymentRepository.findAllByConsumer(consumer);
        updateAllPaymentStatus(request, allPayment);

    }

    private void updateAllPaymentStatus(PayOneRequest request, List<Payment> allPayment) {
        int totalSum = 0;
        for (Payment payment : allPayment) {
            totalSum += payment.getPayTotal();
        }
        if(request.getPayTotal()!=totalSum){
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        allPayment.forEach(this::updatePayStatus);
    }
}
