package hello.servlet.web.servletMvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "mvcMemberFormServlet", urlPatterns = "/servlet-mvc/members/new-form")
public class MvcMemberFormServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String viewPath = "/WEB-INF/views/new-form.jsp";
        // viewPath로 이동할 때 사용함
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        //서버 내부에서 호출이 다시 발생
        dispatcher.forward(request, response);
//        res.getWriter().write("ok");
    }
}
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//@WebServlet(name = "mvcMemberFormServlet", urlPatterns = "/servlet-mvc/members/new-form")
//        public class MvcMemberFormServlet extends HttpServlet {
//        @Override
//        protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
//        {
//              String viewPath = "/WEB-INF/views/new-form.jsp";
//              RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
//              dispatcher.forward(request, response);
//              }
//}