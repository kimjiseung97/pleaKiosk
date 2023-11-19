package kiosk.pleaKiosk.domain.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kiosk.pleaKiosk.domain.entity.Order;
import kiosk.pleaKiosk.domain.entity.Product;
import kiosk.pleaKiosk.domain.entity.QOrder;
import kiosk.pleaKiosk.domain.entity.QProduct;
import lombok.RequiredArgsConstructor;
import org.hibernate.criterion.Projection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class OrderCustomImpl implements OrderCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Order> getAllOrderList(Product product, Pageable pageable) {

        QOrder order = QOrder.order;

        QueryResults<Order> orderQueryResults = jpaQueryFactory.select(order)
                .from(order)
                .where(order.product.eq(product))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(orderQueryResults.getResults(), pageable, orderQueryResults.getTotal());
    }
}
