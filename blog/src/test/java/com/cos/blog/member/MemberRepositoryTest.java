package com.cos.blog.member;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void create() {
        Member member =
                Member.builder()
                        .name("test")
                        .email("test@gmail.com")
                        .phone_num("01012345678")
                        .created_At(LocalDateTime.now())
                        .status("crated")
                        .build();

        Member newMember = memberRepository.save(member);
        log.info(newMember.toString());
    }

    @Test
    public void read() {
        Optional<Member> findMember = memberRepository.findById(1L);
        findMember.ifPresent(
                selectUser -> {
                    assertThat(selectUser.getName()).isEqualTo("test");
                }
        );
    }

    @Test
    public void update() {
        Optional<Member> findMember = memberRepository.findById(1L);
        findMember.ifPresent(
                selectUser -> {
                    Member newMember =
                            Member.builder()
                                    .Id(selectUser.getId())
                                    .name("test2")
                                    .phone_num(selectUser.getPhone_num())
                                    .email(selectUser.getEmail())
                                    .created_At(selectUser.getCreated_At())
                                    .updated_At(LocalDateTime.now())
                                    .status("updated")
                                    .build();
                    memberRepository.save(newMember);
                }
        );
    }

    @Test
    public void updateRead() {
        create();
        update();
        Optional<Member> findMember = memberRepository.findById(1L);
        findMember.ifPresent(
                selectMember -> {
                    log.info("selectMember = {}", selectMember.toString());
                    assertThat(selectMember.getName()).isEqualTo("test2");
                }
        );
    }
}
