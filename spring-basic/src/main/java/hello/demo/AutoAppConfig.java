package hello.demo;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import static org.springframework.context.annotation.ComponentScan.*;

@Configuration
//@Component 어노테이션이 붙은 모든 클래스들을 스프링 빈으로 등록해준다.
@ComponentScan(
        basePackages = "hello.demo",
        excludeFilters = @Filter(type = FilterType.ANNOTATION, classes = Configuration.class)   //@Coniguration이 붙은것은 빼라
)

public class AutoAppConfig {

}
