package com.cos.blog.member;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String name;

    private String email;

    private String phone_num;

    @Column(nullable = false)
    private LocalDateTime created_At;

    private LocalDateTime updated_At;

    @Column(nullable = false)
    private String status;


}
