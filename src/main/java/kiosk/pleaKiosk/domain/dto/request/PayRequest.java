package kiosk.pleaKiosk.domain.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Schema(description = "결제 요청 dto")
public class PayRequest {

    @NotNull(message = "결제 번호는 필수입니다")
    @Schema(description = "테이블 번호",example = "1")
    @Min(value = 1,message = "음수를 입력할 수 없습니다")
    private Long id;

    @NotNull(message = "결제금액은 필수입니다")
    @Min(value = 1,message = "음수를 입력할 수 없습니다")
    @Schema(description = "결제요청 금액(모자르면 결제 안됌)",example = "10000")
    private int payTotal;

}
