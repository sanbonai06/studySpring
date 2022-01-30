package hello.demo.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class NetworkClient {

    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);

    }

    public void setUrl(String url) {
        this.url = url;
    }

    @PostConstruct
    public void init() {
        connect();
        call("연결되었습니다.");
    }
    //서비스 시작 시 호출
    public void connect() {
        System.out.println("의존관계 주입 완료");
        System.out.println("connect: " + url);
    }

    public void call(String message) {
        System.out.println("call: " + url + " message:" + message);
    }

    @PreDestroy
    public void close() {
        System.out.println("연결을 끊습니다.");
        System.out.println("close: " + url);
    }

//    //의존관계 주입이 끝나면,
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        System.out.println("의존관계 주입 완료");
//        connect();
//        call("초기화 연결 메세지");
//    }
//
//    @Override
//    public void destroy() throws Exception {
//        System.out.println("연결을 끊습니다.");
//        disconnect();
//    }
}
