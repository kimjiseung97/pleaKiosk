package kiosk.pleaKiosk.domain.entity;

import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_PAYMENT")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
@EntityListeners(AuditingEntityListener.class)
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    @JoinColumn(name = "CONSUMER_ID")
    private Consumer consumer;

    @Column(name = "PAY_STATUS")
    @Enumerated(EnumType.STRING)
    private PaymentStatus payStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @CreatedDate
    @Column(name = "PAY_DT")
    private LocalDateTime createdDate;

    @Column(name = "PAY_TOTAL")
    private int payTotal;

}
