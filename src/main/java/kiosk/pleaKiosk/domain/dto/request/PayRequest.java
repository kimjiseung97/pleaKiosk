package kiosk.pleaKiosk.domain.dto.request;
import lombok.Getter;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
public class PayRequest {

    @NotNull(message = "결제 번호는 필수입니다")
    @Min(value = 1,message = "음수를 입력할 수 없습니다")
    private Long id;

    @NotNull(message = "결제금액은 필수입니다")
    @Min(value = 1,message = "음수를 입력할 수 없습니다")
    private int payTotal;

}
