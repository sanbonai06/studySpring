package hello.demo.scope;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class SingletonTest {

    @Test
    public void SingletonBeanFind() {

        ApplicationContext ac = new AnnotationConfigApplicationContext(SingletonBean.class);
        SingletonBean bean = ac.getBean(SingletonBean.class);
        bean.close();
    }

    @Scope("singleton")
    static class SingletonBean{

        @PostConstruct
        public void init() {
            System.out.println("SingletonBean.init");
        }

        @PreDestroy
        public void close() {
            System.out.println("SingletonBean.close");
        }
    }
}
