package kiosk.pleaKiosk.domain.entity;
import lombok.*;
import javax.persistence.*;


@Entity
@Table(name = "TB_TABLE")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Consumer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

}
