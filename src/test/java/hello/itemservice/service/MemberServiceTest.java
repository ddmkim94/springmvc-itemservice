package hello.itemservice.service;

import hello.itemservice.domain.Address;
import hello.itemservice.domain.Member;
import hello.itemservice.repository.MemberRepositoryOld;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 스프링 부트를 띄우고 테스트 (없는 경우 @Autowired 전부 실패)
@Transactional // 테스트가 끝나면 트랜잭션 강제로 롤백 (테스트 클래스에서만 이렇게 동작)
class MemberServiceTest {

    @Autowired
    MemberRepositoryOld memberRepository;
    @Autowired MemberService memberService;

    @Test
    @DisplayName("회원가입")
    void join() throws Exception {
        //given
        Address address = new Address("seoul", "eunbinro", "0904");
        Member member = new Member("은빈", address);

        //when
        Long saveId = memberService.join(member);

        //then
        assertThat(saveId).isEqualTo(member.getId());
        Member findMember = memberRepository.findById(saveId);
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    @DisplayName("회원중복검사")
    void validateMember() throws Exception {
        //given
        Address address = new Address("seoul", "eunbinro", "0904");

        Member member1 = new Member("은빈", address);
        Member member2 = new Member("은빈", address);

        //when, then
        memberService.join(member1);
        assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        /*
        assertThatIllegalArgumentException()
                .isThrownBy(() -> memberService.join(member2))
        */


    }
}