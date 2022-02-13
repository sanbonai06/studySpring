package hello.test.miniproject.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
public class Member {

    private Long id;
    private String name;
    private Integer age;

    public Member() {

    }

    public Member(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
