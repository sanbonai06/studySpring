package hello.servlet.basic.response;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "responseHeaderServlet", urlPatterns = "/response-header")
public class ResponseHeaderServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        //status-line
        res.setStatus(HttpServletResponse.SC_OK);

        //response-header
        res.setHeader("Content-Type", "text/plain;charset=utf-8");
        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        res.setHeader("Pragma", "no-cache");
        res.setHeader("My-Header", "hello");

        //header 편의 메서드
        content(res);

        //cookie 편의 메서드
        cookie(res);

        //redirect 편의 메서드
        redirect(res);

        //message body
        res.getWriter().write("ok");
    }

    private void content(HttpServletResponse response) {
    //Content-Type: text/plain;charset=utf-8
    //Content-Length: 2
    //response.setHeader("Content-Type", "text/plain;charset=utf-8");
     response.setContentType("text/plain");
     response.setCharacterEncoding("utf-8");
     //response.setContentLength(2); //(생략시 자동 생성)
    }

    private void cookie(HttpServletResponse response) {
    //Set-Cookie: myCookie=good; Max-Age=600; //response.setHeader("Set-Cookie", "myCookie=good; Max-Age=600");
     Cookie cookie = new Cookie("myCookie", "good");
     cookie.setMaxAge(600); //600초
     response.addCookie(cookie);
    }

    private void redirect(HttpServletResponse response) throws IOException {
        //Status Code 302
        //Location: /basic/hello-form.html
        //response.setStatus(HttpServletResponse.SC_FOUND); //302
        //response.setHeader("Location", "/basic/hello-form.html");
        response.sendRedirect("/basic/hello-form.html");
    }
}
