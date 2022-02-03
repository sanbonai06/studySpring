package hello.servlet.basic.request;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@WebServlet(name = "requestParamServlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        System.out.println("전체 파라미터 조회 - Start");

        //paramName은 키값 (name) req.getParameter(paramName)는 밸류값(kim)
        req.getParameterNames().asIterator()
                        .forEachRemaining(paramName -> System.out.println(paramName  + " = " + req.getParameter(paramName)));

        System.out.println("전체 파라미터 조회 - end");
        System.out.println();

        System.out.println("단일 파라미터 조회 - start");
        String userName = req.getParameter("userName");
        String age = req.getParameter("age");
        System.out.println("userName = " + userName);
        System.out.println("age = " + age);
    }
}
