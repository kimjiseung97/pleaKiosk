package kiosk.pleaKiosk.domain.dto.response;

import kiosk.pleaKiosk.domain.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class OrderRegisterResponse {

    private Long productId;

    private String productName;

    private Long orderId;

    private Long consumerId;

    private int amount;

    private LocalDateTime createdDate;

    private OrderStatus orderStatus;

}
