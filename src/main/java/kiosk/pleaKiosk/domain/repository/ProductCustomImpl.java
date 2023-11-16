package kiosk.pleaKiosk.domain.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kiosk.pleaKiosk.domain.entity.Product;
import kiosk.pleaKiosk.domain.entity.QProduct;
import lombok.RequiredArgsConstructor;
import org.hibernate.criterion.Projection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class ProductCustomImpl implements ProductCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Product> findAllProduct(Pageable pageable) {

        QProduct pd = QProduct.product;

        QueryResults<Product> results = jpaQueryFactory
                .select(Projections.constructor(Product.class, pd.id, pd.name, pd.price,pd.amount,pd.createdDate,pd.lastModifiedDate))
                .from(pd)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }
}
