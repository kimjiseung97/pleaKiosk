package kiosk.pleaKiosk.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "TB_USER")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false,name = "NAME")
    private String name;

    @OneToMany(mappedBy = "user",fetch =FetchType.LAZY)
//    @BatchSize(size = 50) // 예시: 한 번에 10개의 Article 가져오기
    private List<Article> articles;
}
