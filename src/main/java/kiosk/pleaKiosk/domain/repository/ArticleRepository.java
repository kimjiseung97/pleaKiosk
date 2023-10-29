package kiosk.pleaKiosk.domain.repository;

import kiosk.pleaKiosk.domain.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article,Long> {
}
