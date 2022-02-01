package hello.demo.scope;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.assertj.core.api.Assertions.*;

public class PrototypeTest {

    @Test
    public void prototypeBeanFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(prototypeBean.class);
        prototypeBean bean = ac.getBean(prototypeBean.class);
        prototypeBean bean1 = ac.getBean(prototypeBean.class);
        assertThat(bean).isNotSameAs(bean1);

        ac.close();

    }

    @Scope("prototype")
    static class prototypeBean{

        @PostConstruct
        public void init() {
            System.out.println("prototypeBean.init");
        }

        @PreDestroy
        public void close() {
            System.out.println("prototypeBean.close");
        }
    }
}
