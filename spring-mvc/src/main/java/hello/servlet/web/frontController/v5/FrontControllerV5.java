package hello.servlet.web.frontController.v5;

import hello.servlet.web.frontController.ModelView;
import hello.servlet.web.frontController.MyHandlerAdaptor;
import hello.servlet.web.frontController.MyView;
import hello.servlet.web.frontController.v3.contorller.MemberFormControllerV3;
import hello.servlet.web.frontController.v3.contorller.MemberListControllerV3;
import hello.servlet.web.frontController.v3.contorller.MemberSaveControllerV3;
import hello.servlet.web.frontController.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontController.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontController.v4.controller.MemberSaveControllerV4;
import hello.servlet.web.frontController.v5.adaptor.ControllerV3HandlerAdaptor;
import hello.servlet.web.frontController.v5.adaptor.ControllerV4HandlerAdaptor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "frontControllerV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerV5 extends HttpServlet {

    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<MyHandlerAdaptor> handlerAdaptors = new ArrayList<>();

    public FrontControllerV5() {
        initHandlerMappingMap();
        initHandlerAdaptors();
    }

    private void initHandlerAdaptors() {
        handlerAdaptors.add(new ControllerV3HandlerAdaptor());
        handlerAdaptors.add(new ControllerV4HandlerAdaptor());
    }

    private void initHandlerMappingMap() {
        //v3
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());

        //v4
        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());

    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        Object handler = getHandler(req);

        if(handler == null) {
            res.setStatus(404);
            return;
        }

        MyHandlerAdaptor handlerAdaptor = getHandlerAdaptor(handler);
        ModelView mv = handlerAdaptor.handle(req, res, handler);
        String viewName = mv.getViewName();
        MyView myView = viewResolver(viewName);
        myView.render(mv.getModel(), req, res);

    }

    private Object getHandler(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        return handlerMappingMap.get(requestURI);
    }

    private MyHandlerAdaptor getHandlerAdaptor(Object handler) {
        for (MyHandlerAdaptor handlerAdaptor : handlerAdaptors) {
            if(handlerAdaptor.supports(handler)) {
                return handlerAdaptor;
            }
        }
        throw new IllegalArgumentException("handler adaptor를 찾을 수 없습니다. handler = " + handler);
    }

    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }
}
