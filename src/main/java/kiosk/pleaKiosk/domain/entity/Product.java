package kiosk.pleaKiosk.domain.entity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "TB_PRODUCT")
@Getter
@ToString
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PRICE")
    private int price;

    @Column(name = "AMOUNT")
    private int amount;

    @CreatedDate
    @Column(name = "CREATE_DT")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DT")
    private LocalDateTime lastModifiedDate;

    public void updateEntity(String updateName, int updatePrice, int updateAmount){
        this.name = updateName;
        this.price = updatePrice;
        this.amount = updateAmount;
        this.lastModifiedDate = LocalDateTime.now();
    }

}
