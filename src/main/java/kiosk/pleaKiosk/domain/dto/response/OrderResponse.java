package kiosk.pleaKiosk.domain.dto.response;

import kiosk.pleaKiosk.domain.entity.OrderStatus;
import kiosk.pleaKiosk.domain.entity.Product;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@AllArgsConstructor
@Builder
@Getter
public class OrderResponse {

    private Long productId;

    private String productName;

    private Long orderId;

    private Long consumerId;

    private int amount;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    private OrderStatus orderStatus;


}
