package hello.servlet.web.springmvc.v2;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/springmvc/v2/members")
public class SpringMemberControllerV2 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @RequestMapping("/new-form")
    public ModelAndView newFormProcess() {
        System.out.println("SpringMemberControllerV2.newFormProcess");
        return new ModelAndView("new-form");
    }

    @RequestMapping("/save")
    public ModelAndView saveProcess(@RequestParam Map<String, String> paramMap) {
        System.out.println("SpringMemberControllerV2.saveProcess");

        String username = paramMap.get("username");
        int age = Integer.parseInt(paramMap.get("age"));

        Member member = new Member(username, age);
        System.out.println("member = " + member.getId());

        memberRepository.save(member);

        ModelAndView mv = new ModelAndView("save-result");
        mv.addObject(member);

        return mv;
    }

    @RequestMapping
    public ModelAndView showListProcess() {
        System.out.println("SpringMemberControllerV2.showListProcess");

        List<Member> members = memberRepository.findAll();

        ModelAndView mv = new ModelAndView("members");
        mv.addObject("members", members);

        return mv;
    }
}
