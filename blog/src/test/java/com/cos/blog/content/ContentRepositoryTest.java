package com.cos.blog.content;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@Slf4j
@SpringBootTest
public class ContentRepositoryTest {

    @Autowired
    private ContentRepository contentRepository;

    @Test
    public void save() {
        Content newContent = Content.builder()
                .scripts("테스트용 스크립트 입니다.")
                .writerId(1L)
                .build();
        contentRepository.save(newContent);
    }

    @Test
    public void findById() {
        save();
        Optional<Content> findContent = contentRepository.findById(1L);
        findContent.ifPresent(
                selectContent -> {
                    log.info("scripts = {}", selectContent.getScripts());
                    Assertions.assertThat(selectContent.getWriterId()).isEqualTo(1L);
                }
        );
    }
}
