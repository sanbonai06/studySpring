package hello.demo.scope;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Provider;

import static org.assertj.core.api.Assertions.*;

public class SingletonWithPrototypeTest1 {

    @Test
    public void prototypeFind() {

        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ProtoBean.class);
        ProtoBean bean = ac.getBean(ProtoBean.class);
        ProtoBean bean1 = ac.getBean(ProtoBean.class);

        bean.addCount();
        bean1.addCount();

        int count1 = bean.getCount();
        int count2 = bean1.getCount();
        System.out.println("count1 = " + count1);
        System.out.println("count2 = " + count2);

        assertThat(count1).isEqualTo(count2);

        ac.close();
    }

    @Test
    public void singletonFind() {

        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SingleBean.class);
        SingleBean bean = ac.getBean(SingleBean.class);
        SingleBean bean1 = ac.getBean(SingleBean.class);

        bean.addCount();
        bean1.addCount();

        int count1 = bean.getCount();
        int count2 = bean.getCount();

        System.out.println("count1 = " + count1);
        System.out.println("count2 = " + count2);

        assertThat(count1).isEqualTo(count2);

        ac.close();
    }

    @Test
    public void singletonClientUsePrototype() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ProtoBean.class, ClientBean.class);
//        AnnotationConfigApplicationContext ac1 = new AnnotationConfigApplicationContext(ClientBean.class);

        //ProtoBean protoBean = ac.getBean(ProtoBean.class);
        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        ClientBean clientBean2 = ac.getBean(ClientBean.class);

        int result1 = clientBean1.logic();
        int result2 = clientBean2.logic();

        System.out.println("result1 = " + result1);
        System.out.println("result2 = " + result2);

        assertThat(result1).isEqualTo(result2);


    }


    @Scope("prototype")
    static class ProtoBean{
        private int count = 0;
        public void addCount() {
            count++;
        }

        public int getCount() {
            return count;
        }

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init " + this);
        }

        @PreDestroy
        public void destroy() {
            System.out.println("ProtoBean.destroy");
        }
    }

    @Scope("singleton")
    static class ClientBean{

        //private final ProtoBean protoBean;      //생성시점에 이미 프로토빈이 주입되어버림,, 따라서 여러번 클라이언트빈이 호출되도 똑같은 프로토빈이 사용됨

        @Autowired
        private Provider<ProtoBean> protoBeanProvider;

        public int logic() {
            ProtoBean protoBean = protoBeanProvider.get();
            protoBean.addCount();
            int count = protoBean.getCount();
            return count;
        }
    }


    @Scope("singleton")
    static class SingleBean{
        private int count = 0;
        public void addCount() {
            count++;
        }

        public int getCount() {
            return count;
        }

        @PostConstruct
        public void init() {
            System.out.println("SingletonBean.init " + this);
        }

        @PreDestroy
        public void destroy() {
            System.out.println("SingleBean.destroy");
        }
    }
}
