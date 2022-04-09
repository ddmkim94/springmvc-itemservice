package hello.itemservice.service;

import hello.itemservice.domain.Member;
import hello.itemservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // readOnly = true -> 읽기 전용
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     */
    @Transactional(readOnly = false) // 변경 메서드는 false로 설정
    public Long join(Member member) {
        // 중복 회원 검증
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) { // 중복 이름을 가진 회원이 있다면
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 정보 수정
     * - 변경 감지를 통한 데이터 변경
     */
    @Transactional
    public void update(Long memberId, String updateName) {
        Member findMember = memberRepository.findById(memberId);
        findMember.changeMember(updateName);
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    /**
     * 회원 조회 (식별자)
     */
    public Member findById(Long id) {
        return memberRepository.findById(id);
    }
}