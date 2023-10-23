package kiosk.pleaKiosk.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderModifyRequest {

    @NotNull(message = "주문번호는 필수입니다")
    @Min(value = 1,message = "음수를 입력할 수 없습니다")
    private Long id;

    @NotNull(message = "상품번호는 필수입니다.")
    @Min(value = 1,message = "음수를 입력할 수 없습니다")
    private Long productId;

    @NotNull(message = "수량은 필수입니다.")
    @Min(value = 1,message = "음수를 입력할 수 없습니다")
    private int amount;
}
