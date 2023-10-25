package kiosk.pleaKiosk.domain.dto.response;

import kiosk.pleaKiosk.domain.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class OrderModifyResponse {

    private Long id;

    private Long consumerId;

    private Long productId;

    private String productName;

    private int amount;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    private OrderStatus orderStatus;

}
