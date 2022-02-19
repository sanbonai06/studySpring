package hello.login.web.session;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest req) {
        HttpSession session = req.getSession();
        if (session == null) {
            return "세션이 없습니다.";
        }

        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name = {}, value = {}", name, session.getAttribute(name)));

        return "세션 출력";
    }
}
