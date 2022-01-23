package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("data", "사용자님!!");
        return "hello";
    }
    //view방식
    @GetMapping("hello-mvc")    //http메서드 정의
    public String helloMvc(@RequestParam("name") String name, Model model) {    //파라미터, 바디, pathvriable 등등
        model.addAttribute("name", name);
        return "hello-template";        //띄울 view 파일들
    }

    //API방식
    @GetMapping("hello-string")
    @ResponseBody   //응답 body에 리턴 값을 직접 넣겠다
    public String helloString(@RequestParam("name") String name) {
        return "hello " + name;
    }

    @GetMapping("welcome")
    @ResponseBody
    public Welcome welcomeApi(@RequestParam("name") String name) {
        Welcome welcome = new Welcome();
        welcome.setUserName(name);
        return welcome;
    }

    static class Welcome {
        private String userName;

        public String getUserName() {
            return this.userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
    //JSON으로 넘어감
    @GetMapping("hello-api")
    @ResponseBody   //이 어노테이션이 붙어있으면 viewResolve를 찾지 않고 바로 리턴값을 응답 body에 넣어서 전송(httpMessageConverter가 동작)
    public Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello();
        hello.setName(name);
        return hello;
    }

    static class Hello {
        private String name;

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
