package kiosk.pleaKiosk.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@Schema(description = "주문삭제 요청 dto")
public class OrderDeleteRequest {

    @NotNull(message = "주문번호는 필수입니다")
    @Min(value = 1,message = "음수를 입력할 수 없습니다")
    @Schema(description = "주문번호",example = "1")
    private Long id;
}
