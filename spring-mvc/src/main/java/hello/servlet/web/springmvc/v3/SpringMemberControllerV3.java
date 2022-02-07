package hello.servlet.web.springmvc.v3;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/springmvc/v3/members")
public class SpringMemberControllerV3 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @GetMapping("/new-form")
    public String newFormProcess() {
        System.out.println("SpringMemberControllerV3.newFormProcess");
        return "new-form";
    }

    @PostMapping("/save")
    public String saveProcess(
            @RequestParam("username") String username,
            @RequestParam("age") int age,
            Model model
    ) {
        System.out.println("SpringMemberControllerV3.saveProcess");

        Member member = new Member(username, age);
        System.out.println("member = " + member.getId());

        memberRepository.save(member);

        model.addAttribute("member", member);

        return "save-result";
    }

    @GetMapping
    public String showListProcess(Model model) {
        System.out.println("SpringMemberControllerV3.showListProcess");

        List<Member> members = memberRepository.findAll();

        model.addAttribute("members", members);

        return "members";
    }
}
