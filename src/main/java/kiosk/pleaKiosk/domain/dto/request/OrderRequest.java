package kiosk.pleaKiosk.domain.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@ToString
@Schema(title = "주문요청 정보")
public class OrderRequest {

    @Schema(title = "상품번호", example = "1",description = "상품 고유번호입니다.")
    @NotNull(message = "상품번호는 필수입니다")
    @Min(value = 1,message = "음수를 입력할 수 없습니다")
    private Long productId;

    @Schema(title = "주문수량", example = "1",description = "상품 주문수량입니다.")
    @Min(value = 1,message = "주문수량은 최소 1개이상입니다")
    @Max(value = 9999,message ="수량은 1천 단위까지만 가능합니다.")
    private int amount;

    @Schema(title = "테이블번호", example = "1",description = "주문요청하는 테이블 번호입니다.")
    @NotNull(message = "테이블번호는 필수입니다.")
    @Min(value = 1, message = "음수는 입력할 수 없습니다.")
    private Long tableId;

}
