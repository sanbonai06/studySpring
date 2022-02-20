package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] whiteList = {"/", "members/add", "/login", "/logout", "/css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String requestURI = req.getRequestURI();

        HttpServletResponse res = (HttpServletResponse) response;

        try{
            log.info("인증 체크 필터 시작 {}", requestURI);

            if (isLoginCheck(requestURI)) {
                log.info("인증 체크 로직 실행 {}", requestURI);
                HttpSession session = req.getSession(false);
                if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
                    log.info("인증하지 않은 사용자 요청 {}", requestURI);
                    //로그인 페이지로 리다이렉트 시키되 로그인 시 다시 이페이지로 리다이렉트
                    res.sendRedirect("/login?redirectURL=" + requestURI);
                    return; //미인증 사용자는 더이상 진행하지 않고 여기서 끝
                }
            }

            chain.doFilter(request, response);
        }catch (Exception e){
            throw e;    //예외 로깅 처리 가능하지만 톰캣 까지 예외를 올려줘야함 아니면 정상 흐름대로 동작 하는 것 처럼 보임
        }finally {
            log.info("인증 체크 필터 종료 {}", requestURI);
        }
    }

    /**
     * 화이트리스트의 경우 인증 체크 로직이 동작 안해도 됨
     */
    private boolean isLoginCheck(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whiteList, requestURI);
    }
}
