package kiosk.pleaKiosk.domain.dto.request;

import kiosk.pleaKiosk.domain.entity.OrderStatus;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class OrderJudgeRequest {

    @NotNull(message = "주문번호는 필수입니다")
    @Min(value = 1,message = "음수를 입력할 수 없습니다")
    private Long id;

    @NotBlank(message = "상태값은 필수입니다")
    private OrderStatus orderStatus = OrderStatus.valueOf("APPROVED");

}
