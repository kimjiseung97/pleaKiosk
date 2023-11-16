package kiosk.pleaKiosk.domain.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Schema(description = "결제 일괄 요청 dto")
public class PayOneRequest {

    @NotNull(message = "테이블 번호는 필수입니다")
    @Schema(description = "테이블 번호",example = "1")
    @Min(value = 1,message = "최소 1이상이어야합니다")
    private Long consumerId;

    @NotNull(message = "총 결제금액은 필수입니다.")
    @Schema(description = "결제요청 금액",example = "10000")
    @Min(value = 1,message = "음수를 입력할 수 없습니다")
    private int payTotal;
}
