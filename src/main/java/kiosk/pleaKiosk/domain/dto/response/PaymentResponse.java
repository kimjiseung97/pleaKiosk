package kiosk.pleaKiosk.domain.dto.response;


import kiosk.pleaKiosk.domain.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class PaymentResponse {

    private Long paymentId;

    private Long consumerId;

    private PaymentStatus paymentStatus;

    private Long OrderId;

    private LocalDateTime payDateTime;

    private int payTotal;
}
