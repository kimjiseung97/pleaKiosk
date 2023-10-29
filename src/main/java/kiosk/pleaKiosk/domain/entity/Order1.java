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
@Table(name = "TB_ORDER1")
@Getter
@NoArgsConstructor
@Setter
@Builder
@AllArgsConstructor
@ToString(exclude = "order1Histories")
@EntityListeners(AuditingEntityListener.class)
public class Order1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private Long id;

    private LocalDateTime orderTime;

    @OneToMany(mappedBy = "order1",fetch = FetchType.EAGER)
    private List<Order1History> order1Histories = new ArrayList<>();

}
