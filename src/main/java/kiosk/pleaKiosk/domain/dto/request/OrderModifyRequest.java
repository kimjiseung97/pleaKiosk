package kiosk.pleaKiosk.domain.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "주문 수정 요청 dto")
public class OrderModifyRequest {

    @NotNull(message = "주문번호는 필수입니다")
    @Min(value = 1,message = "음수를 입력할 수 없습니다")
    @Schema(description = "주문 번호",example = "1")
    private Long id;

    @NotNull(message = "상품번호는 필수입니다.")
    @Min(value = 1,message = "음수를 입력할 수 없습니다")
    @Schema(description = "상품 번호",example = "1")
    private Long productId;

    @NotNull(message = "수량은 필수입니다.")
    @Min(value = 1,message = "음수를 입력할 수 없습니다")
    @Schema(description = "주문 수량",example = "5")
    private int amount;
}
