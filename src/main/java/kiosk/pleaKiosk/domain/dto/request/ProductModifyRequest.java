package kiosk.pleaKiosk.domain.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "상품정보 수정 요청 dto")
public class ProductModifyRequest {

    @NotNull(message = "상품번호는 필수입니다")
    @Schema(description = "상품번호",example = "1")
    private Long id;

    @NotBlank(message = "상품명은 필수입니다")
    @Schema(description = "상품명",example = "라면")
    private String name;

    @NotNull(message = "가격은 필수입니다")
    @Min(value = 3000,message = "가격은 최소3000원 이상입니다")
    @Max(value = 999999,message ="가격은 최대 100만원 단위이상 입력할 수 없습니다.")
    @Schema(description = "가격",example = "3000")
    private int price;

    @Min(value = 1,message = "상품수량은 최소 1개이상 등록해야합니다")
    @Max(value = 9999,message ="수량은 1천 단위까지만 가능합니다.")
    @Schema(description = "상품수량",example = "500")
    private int amount;
}
