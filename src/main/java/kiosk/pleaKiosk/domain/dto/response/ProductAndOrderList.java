package kiosk.pleaKiosk.domain.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@Builder
@Getter
public class ProductAndOrderList {

    ProductResponse productResponse;

    Page<OrderList> orderList;

}
