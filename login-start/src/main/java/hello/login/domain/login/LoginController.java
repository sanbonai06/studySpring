package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm loginForm) {
        return "login/loginForm";
    }

    //@PostMapping("/login")
    public String loginMember(@Valid @ModelAttribute("loginForm") LoginForm loginForm,
                              BindingResult bindingResult, HttpServletResponse res) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member login = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        log.info("login? {}", login);

        if (login == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        Cookie idCookie = new Cookie("memberId", String.valueOf(login.getId()));

        res.addCookie(idCookie);

        return "redirect:/";
    }

    //@PostMapping("/login")
    public String loginMemberV2(@Valid @ModelAttribute("loginForm") LoginForm loginForm,
                                BindingResult bindingResult, HttpServletResponse res) {

        if(bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member member = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if (member == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        sessionManager.createSession(member, res);
        return "redirect:/";
    }

    //@PostMapping("/login")
    public String loginMemberV3(@Valid @ModelAttribute("loginForm") LoginForm loginForm,
                                BindingResult bindingResult, HttpServletRequest req, HttpServletResponse res) {

        if(bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member member = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if (member == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //sessionManager.createSession(member, res);
        //session이 있으면 있는 session 반환, 없으면 신규 session 생성해서 반환
        HttpSession session = req.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);

        return "redirect:/";
    }

    @PostMapping("/login")
    public String loginMemberV4(@Valid @ModelAttribute("loginForm") LoginForm loginForm,
                                BindingResult bindingResult, @RequestParam(defaultValue = "/") String redirectURL,
                                HttpServletRequest req, HttpServletResponse res) {

        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member member = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if (member == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        HttpSession session = req.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);

        return "redirect:" + redirectURL;
    }


//    @PostMapping("/logout")
    public String logoutMember(HttpServletResponse res) {
        expireCookie(res, "memberId");
        return "redirect:/";
    }

    //@PostMapping("/logout")
    public String logoutMemberV2(HttpServletRequest req) {
        sessionManager.expire(req);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logoutMemberV3(HttpServletRequest req) {
//        sessionManager.expire(req);
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }


    private void expireCookie(HttpServletResponse res, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        res.addCookie(cookie);
    }
}
