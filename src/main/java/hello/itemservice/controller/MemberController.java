package hello.itemservice.controller;

import hello.itemservice.domain.Address;
import hello.itemservice.domain.Member;
import hello.itemservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Member member = new Member(form.getName(), new Address(form.getCity(), form.getStreet(), form.getZipcode()));
        memberService.join(member);

        return "redirect:/";
    }

    @GetMapping("members")
    public String list(Model model) {
        // 리팩토링 할 부분! => Member 엔티티를 반환하지말고, 필요한 데이터만 가진 DTO를 만들어서 리턴하자!
        List<Member> findMembers = memberService.findAll();
        List<MemberSearchDTO> members = new ArrayList<>();
        for (Member member : findMembers) {
            members.add(new MemberSearchDTO(member.getId(), member.getName(), member.getAddress()));
        }
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
