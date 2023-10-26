package kiosk.pleaKiosk.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ProductResponse {

    private Long id;

    private String productName;

    private int amount;
}
