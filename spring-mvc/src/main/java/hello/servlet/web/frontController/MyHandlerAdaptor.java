package hello.servlet.web.frontController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface MyHandlerAdaptor {

    boolean supports(Object handler);

    ModelView handle(HttpServletRequest req, HttpServletResponse res, Object handler) throws ServletException, IOException;
}
