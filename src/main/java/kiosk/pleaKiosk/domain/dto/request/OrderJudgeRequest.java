package kiosk.pleaKiosk.domain.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import kiosk.pleaKiosk.domain.entity.OrderStatus;
import lombok.Getter;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Schema(description = "주문 상태변경 요청 dto")
public class OrderJudgeRequest {

    @NotNull(message = "주문번호는 필수입니다")
    @Min(value = 1,message = "음수를 입력할 수 없습니다")
    @Schema(description = "주문번호", example = "1")
    private Long id;

    @NotNull(message = "상태값은 필수입니다")
    @Schema(description = "주문 상태값",implementation = OrderStatus.class)
    private OrderStatus orderStatus = OrderStatus.valueOf("APPROVED");

}
