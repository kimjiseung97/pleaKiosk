package kiosk.pleaKiosk.domain.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kiosk.pleaKiosk.domain.entity.*;
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
        QProduct product1 = QProduct.product; // 상품 엔티티도 Q타입으로 생성

        QueryResults<Order> orderQueryResults = jpaQueryFactory
                .select(order)
                .from(order)
                .join(order.product, product1) // join 구문 추가
                .where(order.product.eq(product)) // where 조건 수정
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(orderQueryResults.getResults(), pageable, orderQueryResults.getTotal());

    }

    @Override
    public Page<Order> findByConsumer(Long consumerId, Pageable pageable) {

        QOrder order = QOrder.order;
        QConsumer consumer = QConsumer.consumer;
        QProduct product = QProduct.product; // 상품 엔티티도 Q타입으로 생성

        QueryResults<Order> orderQueryResults = jpaQueryFactory
                .select(order)
                .from(order)
                .join(order.consumer, consumer)
                .fetchJoin() // fetch join 추가
                .join(order.product, product)
                .fetchJoin() // fetch join 추가
                .where(consumer.id.eq(consumerId)) // where 조건 수정
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(orderQueryResults.getResults(), pageable, orderQueryResults.getTotal());

    }


}
