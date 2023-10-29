package kiosk.pleaKiosk.domain.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "TB_ORDER1HISTORY")
@Getter
@NoArgsConstructor
@Setter
@Builder
@AllArgsConstructor
@ToString(exclude = "order1")
@EntityListeners(AuditingEntityListener.class)
public class Order1History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HISTORY_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ORDER1_ID")
    private Order1 order1;

}
