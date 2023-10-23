package kiosk.pleaKiosk.domain.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class PayOneRequest {

    @NotNull(message = "테이블 번호는 필수입니다")
    private Long consumerId;
    @NotNull(message = "총 결제금액은 필수입니다.")
    private int payTotal;
}
