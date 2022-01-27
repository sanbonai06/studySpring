package hello.demo.singleton;

public class SingletonService {

    //자기 자신을 static으로 가지고 있기 때문에 딱 한개만 가지고 있게 된다.
    private static final SingletonService instance = new SingletonService();

    //만들어진 싱글톤을 끌어오는 방법
    public static SingletonService getInstance() {
        return instance;
    }

    //프라이빗 생성자로 만들어서 외부에서 호출되는 걸 막음
    private SingletonService() {
    }

    public void logic() {
        System.out.println("싱글톤 로직을 호출함");
    }
}
