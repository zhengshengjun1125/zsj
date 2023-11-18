package com.zsj.article;

import com.zsj.article.entity.EntityToForce;
import com.zsj.article.service.EntityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@SpringBootTest
class ZsjArticleApplicationTests {


    @Autowired
    @Lazy
    EntityService entityService;

    @Test
    void contextLoads() {
        List<EntityToForce> allArticle =
                entityService.getAllArticle();
        allArticle.forEach(System.out::println);
    }

}
