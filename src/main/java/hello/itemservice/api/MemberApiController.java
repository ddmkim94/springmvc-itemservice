package hello.itemservice.api;

import hello.itemservice.domain.Address;
import hello.itemservice.domain.Member;
import hello.itemservice.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

// @Controller + @ResponseBody
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 등록 V1: 파라미터로 엔티티를 직접 받는다.
     * V1의 문제점 : 엔티티를 파라미터로 직접 사용
     * - name을 안넘겨도 회원 등록이 가능함 -> 검증을 위해 엔티티에 화면 관련 코드가 들어감 (@NotEmpty)
     */
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long memberId = memberService.join(member);
        return new CreateMemberResponse(memberId);
    }

    /**
     * 등록 V2: 파라미터로 별도의 DTO를 받는다.
     * - 프레젠테이션 계층의 코드를 DTO에 작성하고, 엔티티는 순수하게 유지가 가능해짐!!
     */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member(request.getName(), request.getAddress());
        Long memberId = memberService.join(member);

        return new CreateMemberResponse(memberId);
    }

    /**
     * 수정 API
     */
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long memberId, @RequestBody @Valid UpdateMemberRequest request) {
        memberService.update(memberId, request.getName());
        Member findMember = memberService.findById(memberId);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    // 변경할 데이터로 name을 받아서 바인딩
    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    // 반환할 DTO
    @Data
    static class UpdateMemberResponse {
        private Long id;
        private String name;

        public UpdateMemberResponse(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    // DTO 역할을 하는 클래스
    @Data
    static class CreateMemberRequest {

        // DTO를 만들어서 사용하면 엔티티에 프레젠테이션 계층의 코드가 들어가지 않음!!
        @NotEmpty(message = "이름은 필수 입력해야합니다~")
        private String name;
        private Address address;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
