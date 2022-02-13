package hello.test.miniproject.controller;

import hello.test.miniproject.MemberData;
import hello.test.miniproject.domain.Member;
import hello.test.miniproject.repository.MemoryMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemoryMemberRepository memoryMemberRepository;

    @PostMapping("/save")
    public String saveMember(@RequestBody MemberData memberData) {
        saveValidation(memberData.getName());
        Member member = new Member(memberData.getName(), memberData.getAge());
        memoryMemberRepository.memberSave(member);
        return "ok";
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<MemberData> findMemberById(@PathVariable Long id) {
        MemberData memberData = new MemberData();
        Optional<Member> findMember = memoryMemberRepository.findById(id);
        if(findMember.isEmpty()){
            return new ResponseEntity<MemberData>(HttpStatus.BAD_REQUEST);
        }
        Member member = findMember.get();
        memberData.setName(member.getName());
        memberData.setAge(member.getAge());
        return new ResponseEntity<MemberData>(memberData, HttpStatus.OK);
    }

    @GetMapping("/findAll")
    public List<Member> findAllMember() {
        return memoryMemberRepository.findAll();
    }

    @GetMapping("/delete/{id}")
    public String deleteMember(@PathVariable Long id) {
        Optional<Member> member = memoryMemberRepository.findById(id);
        if(member.isEmpty()) {
            return "not found Member";
        }
        memoryMemberRepository.deleteMember(id);
        return "delete memberId = " + id;
    }


    private void saveValidation(String name) {
        Optional<Member> member = memoryMemberRepository.findByName(name);
        member.ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 회원 이름입니다.");
        });
    }

    @PostConstruct
    private void init() {
        System.out.println("MemberController.init");
        Member member1 = new Member("test1", 20);
        Member member2 = new Member("test2", 20);
        Member member3 = new Member("test3", 20);
        memoryMemberRepository.memberSave(member1);
        memoryMemberRepository.memberSave(member2);
        memoryMemberRepository.memberSave(member3);
    }
}
