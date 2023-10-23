package kiosk.pleaKiosk.domain.dto.request;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@ToString
public class OrderRequest {

    @NotNull(message = "상품번호는 필수입니다")
    @Min(value = 1,message = "음수를 입력할 수 없습니다")
    private Long productId;

    @Min(value = 1,message = "주문수량은 최소 1개이상입니다")
    @Max(value = 9999,message ="수량은 1천 단위까지만 가능합니다.")
    private int amount;

    @NotNull(message = "테이블번호는 필수입니다.")
    @Min(value = 1, message = "음수는 입력할 수 없습니다.")
    private Long tableId;

}
