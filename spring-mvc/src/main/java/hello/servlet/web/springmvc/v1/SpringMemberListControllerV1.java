package hello.servlet.web.springmvc.v1;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class SpringMemberListControllerV1 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @RequestMapping("springmvc/v1/members")
    public ModelAndView process() {

        System.out.println("SpringMemberListControllerV1.process");
        List<Member> members = memberRepository.findAll();

        System.out.println("members = " + members);
        ModelAndView mv = new ModelAndView("members");
        System.out.println("통과");
        mv.addObject("members", members);
        System.out.println("통과");
        return mv;
    }
}
