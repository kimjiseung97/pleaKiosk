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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@SpringBootTest
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
                .name("유저2")
                .build();

        userRepository.save(user);


        for (int i = 0; i <400 ; i++) {
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
    @Transactional
    public void getArticle(){

        List<User> all = userRepository.findAll();

    }
}