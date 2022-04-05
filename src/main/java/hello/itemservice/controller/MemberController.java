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
        System.out.println(form.getName());
        System.out.println(form.getCity());
        System.out.println(form.getStreet());
        System.out.println(form.getZipcode());

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Member member = new Member(form.getName(), new Address(form.getCity(), form.getStreet(), form.getZipcode()));

        memberService.join(member);

        return "redirect:/";
    }
}
