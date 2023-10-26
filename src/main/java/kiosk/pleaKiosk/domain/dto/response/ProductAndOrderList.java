package kiosk.pleaKiosk.domain.dto.response;


import kiosk.pleaKiosk.domain.entity.Order;
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
