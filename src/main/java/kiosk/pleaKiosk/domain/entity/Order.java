package kiosk.pleaKiosk.domain.entity;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "TB_ORDER")
@Getter
@NoArgsConstructor
@Setter
@Builder
@AllArgsConstructor
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    @JoinColumn(name = "CONSUMER_ID")
    private Consumer consumer;

    @ManyToOne(fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    @Column(name = "AMOUNT")
    private int amount;

    @CreatedDate
    @Column(name = "CREATE_DT")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DT")
    private LocalDateTime lastModifiedDate;

    @Column(name = "ORDER_STATUS")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "PAY_STATUS")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

}
