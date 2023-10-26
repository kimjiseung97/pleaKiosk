package kiosk.pleaKiosk.domain.dto.response;


import kiosk.pleaKiosk.domain.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@AllArgsConstructor
@Builder
public class OrderConfirmResponse {
    private Long id;

    private OrderStatus orderStatus;

}
