package hello.demo.scan.filter;

import java.lang.annotation.*;

//@MyIncludeConent 어노테이션이 붙은 객체는 컴포넌트 스캔을 할 것이다.
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyIncludeComponent {
}
