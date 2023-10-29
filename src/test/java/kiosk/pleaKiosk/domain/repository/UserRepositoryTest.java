package kiosk.pleaKiosk.domain.repository;

import kiosk.pleaKiosk.domain.entity.Article;
import kiosk.pleaKiosk.domain.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.AssertionErrors;

import javax.transaction.Transactional;
import javax.validation.constraints.AssertTrue;
import java.util.Set;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    private final UserRepository userRepository;

    private final ArticleRepository articleRepository;

    @Autowired
    UserRepositoryTest(UserRepository userRepository, ArticleRepository articleRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    @Test
    @DisplayName("유저와 기사들을 저장한다")
    public void save_user(){
        User user = User.builder()
                .name("유저1")
                .build();

        userRepository.save(user);


        for (int i = 0; i <200 ; i++) {
            Article article = Article.builder()
                    .content("테스트 내용입니다" + i)
                    .title("테스트 제목입니다" + i)
                    .user(user)
                    .build();

            articleRepository.save(article);

        }

    }

    @Test
    @DisplayName("유저의 기사들을 가져온다")
    public void getArticle(){
        User user = userRepository.findById(1L).orElseThrow(() -> new NullPointerException());
        String name = user.getName();
//        AssertionErrors.assertTrue("유저 이름은 유저1이다",name.equals("유저1"));

        Set<Article> articles = user.getArticles();

        System.out.println("articles = " + articles);;
    }
}