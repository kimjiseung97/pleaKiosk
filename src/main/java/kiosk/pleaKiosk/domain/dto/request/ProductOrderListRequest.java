package kiosk.pleaKiosk.domain.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class ProductOrderListRequest {

    @NotNull(message = "상품번호는 필수입니다")
    private Long ProductId;

    @NotNull(message = "페이지번호는 필수입니다")
    private int pageNumber;
}
