package hello.login.web.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 세션관리
 */
@Component
public class SessionManager {

    public static final String SESSION_COOKIE_NAME = "mySessionId";
    //동시성 문제(동시에 여러 쓰레드, 요청들이 접근 할 수 있을때는 항상 ConcurrentHashMap을 사용하자.
    private Map<String, Object> sesseionStore = new ConcurrentHashMap<>();

    /**
     * 세션 생성
     */
    public void createSession(Object value, HttpServletResponse res) {

        //sessionId 생성
        String sessionId = UUID.randomUUID().toString();
        sesseionStore.put(sessionId, value);
        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        res.addCookie(cookie);
    }

    /**
     * 세션 조회
     */
    public Object getSession(HttpServletRequest req) {
        Cookie cookie = findCookie(req, SESSION_COOKIE_NAME);
        if (cookie == null) {
            return null;
        }

        return sesseionStore.get(cookie.getValue());
    }

    /**
     * 세션 만료
     */
    public void expire(HttpServletRequest req) {
        Cookie cookie = findCookie(req, SESSION_COOKIE_NAME);
        if (cookie != null) {
            sesseionStore.remove(cookie.getValue());
        }
    }

    private Cookie findCookie(HttpServletRequest req, String cookieName) {
        if (req.getCookies() == null) {
            return null;
        }

        return Arrays.stream(req.getCookies())
                .filter(c -> c.getName().equals(cookieName))
                .findAny()
                .orElse(null);
    }
}
