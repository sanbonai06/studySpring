package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

//    @GetMapping("/")
    public String home() {

        return "home";
    }

    //@GetMapping("/")
    public String loginHome(Model model, HttpServletRequest req) {

        Member member = (Member)sessionManager.getSession(req);

        log.info("member = {}", member);
        if (member == null) {
            return "home";
        }

        model.addAttribute("member", member);
        return "loginHome";

    }

    //@GetMapping("/")
    public String loginHomeV2(Model model, HttpServletRequest req) {

        HttpSession session = req.getSession(false);

        if (session == null) {
            return "home";
        }

        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        log.info("member = {}", member);

        if (member == null) {
            return "home";
        }

        model.addAttribute("member", member);
        return "loginHome";

    }

    @GetMapping("/")
    public String loginHomeV3Spring(Model model,
                                    @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member member) {

        if (member == null) {
            return "home";
        }

        model.addAttribute("member", member);
        return "loginHome";

    }
}