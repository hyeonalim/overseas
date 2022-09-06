/*
 *회원
ㄴ가입
=> 회원 아이디가 테이블에 없는지 검색
=> 비밀번호 와 비밀번호 확인 동일 확인
=> 전화번호 맞는지 검색

ㄴ로그인
=> 회원테이블에 해당 회원 정보가 있는지 확인
=> 세션으로 값 저장

ㄴ탈퇴
=> 회원 테이블에 해당 회원 정보 확인하여 삭제

ㄴ내정보
 ㄴ회원정보
=> 입력하지 않은 정보는 기존 입력 정보로 바꾸기
=> 비밀번호 동일 확인
=> 이 다음 회원 정보 변경되어야 함

 ㄴ주소
=> 회원 테이블에 저장한 주소 정보 가져오기
=> 메인 주소 등록 가능하도록
 */
package sample.shop.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sample.shop.controller.data.MemberForm;
import sample.shop.controller.data.MemberFormLogin;
import sample.shop.controller.data.UpdateMemberRequest;
import sample.shop.domain.Address;
import sample.shop.domain.Cash;
import sample.shop.domain.Grade;
import sample.shop.domain.Member;
import sample.shop.repository.MemberRepository;
import sample.shop.repository.JPA.JPAMemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    private final JPAMemberRepository memberRepositoryJPA;

    private final CashService cashService;

    /**
     * ㄴ가입
     * => 회원 아이디가 테이블에 없는지 검색
     * => 비밀번호 와 비밀번호 확인 동일 확인
     * => 전화번호 맞는지 검색
     */
    @Override
    @Transactional
    public Long join(MemberForm request, String fileUrl) {

        validateDuplicateMember(request.getId()); // 회원아이디중복확인
        checkPwd(request.getPwd(), request.getCheckPwd()); // 회원 비밀번호 일치 확인
        pwd(request.getPwd()); // 회원비밀번호 영어, 특문, 숫자 외 X (8~16문자 사이, 해시함수 사용)
        checkPhone(request.getPhone()); // 전화번호가 맞는지 확인

        Member member = new Member();
        Address address = new Address(request.getCity(), request.getStreet(), request.getZipcode());

        member.setId(request.getId());
        member.setPwd(request.getPwd());
        member.setName(request.getName());
        member.setPhone(request.getPhone());
        member.setAddress(address);
        member.setGrade(Grade.USER);

        if (fileUrl != null) {
            member.setPhotoUrl(fileUrl);
        } else {
            member.setPhotoUrl("/img/upload/KakaoTalk_20220413_004209234.jpg");
        }

        Long cashNo = cashService.create(member);
        Cash cash = cashService.findByNo(cashNo);

        member.setCash(cash);

        memberRepository.save(member);

        return member.getNo();
    }

    // 아이디 비교
    public void validateDuplicateMember(String id) {
        List<Member> findMember = memberRepository.findById(id);

        if (!findMember.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
    }

    // 패스워드 값 비교
    public void checkPwd(String pwd, String checkPwd) {
        if (!pwd.equals(checkPwd)) {
            throw new IllegalStateException("패스워드와 패스워드 확인 값이 일치하지 않습니다.");
        }
    }

    // 패스워드 체크
    public void pwd(String pwd) {

        String pwPattern = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-z])(?=.*[A-Z]).{9,12}$";
        Matcher matcher = Pattern.compile(pwPattern).matcher(pwd);

        pwPattern = "(.)\\1\\1\\1";
        Matcher matcher2 = Pattern.compile(pwPattern).matcher(pwd);

        if (!matcher.matches()) {
        }

        if (matcher2.find()) {
        }

        if (pwd.contains(" ")) {
        }
    }

    public void checkPhone(String phone) {
        String firstPhone = phone.substring(0, 3);

        if (!(firstPhone.equals("010") || firstPhone.equals("070")) && phone.length() == 11) {
            throw new IllegalStateException("핸드폰 번호가 규칙에 맞지 않습니다");
        }
    }

    // 로그인
    /**
     * ㄴ로그인
     * => 회원테이블에 해당 회원 정보가 있는지 확인
     * => 세션으로 값 저장
     */
    @Override
    @Transactional
    public Long login(MemberFormLogin request, HttpServletResponse response) {
        String id = request.getId();
        String pwd = request.getPwd();

        List<Member> findId = memberRepository.findById(id);

        if (findId.isEmpty()) {
            throw new IllegalStateException("등록되어있지 않는 아이디입니다.");
        } else {
            List<Member> findMember = memberRepository.findByMember(id, pwd);

            if (findMember.isEmpty()) {
                throw new IllegalStateException("비밀번호가 틀렸습니다.");
            } else if (findId.size() > 1) {
                throw new IllegalStateException("동일한 회원이 여럿 있습니다.");
            } else {
                return findMember.get(0).getNo();
            }
        }
    }

    @Override
    public Long nowLoginInfo(HttpSession httpSession) {
        try {
            Long memberNo = (Long) httpSession.getAttribute("mSession");

            if (memberNo != null) {
                Member member = myInfo(memberNo);

                if (member == null) {
                    return 0L;
                } else {
                    return member.getNo();
                }
            } else {
                return 0L;
            }
        } catch (Exception e) {
            return 0L;
        }
    }

    // 로그아웃
    @Override
    public void logout(HttpServletResponse response, HttpSession session) {
        session.getAttribute("mSession");
        session.setAttribute("mSession", null);
    }

    // 회원 정보 노출
    @Override
    public List<Member> infos() {
        List<Member> findMember = memberRepository.findAll();

        if (findMember == null) {
            throw new IllegalStateException("등록되어있지 않는 아이디입니다.");
        } else {
            return findMember;
        }
    }

    // 회원 정보 노출 (하나)
    public Member myInfo(Long no) {
        return memberRepository.findOne(no);
    }

    // 회원 업데이트
    /**
     * ㄴ회원정보
     * => 입력하지 않은 정보는 기존 입력 정보로 바꾸기
     * => 비밀번호 동일 확인
     * => 이 다음 회원 정보 변경되어야 함
     */
    @Override
    @Transactional
    public void update(Member member, UpdateMemberRequest request, String fileUrl) {
        if (request.getName().length() != 0) {
            member.setName(request.getName());
        }
        if (request.getId().length() != 0) {
            validateDuplicateMember(request.getId());

            member.setId(request.getId());
        }
        if (request.getPwd().length() != 0) {
            pwd(request.getPwd());
            checkPwd(request.getPwd(), request.getCheckPwd());

            member.setPwd(request.getPwd());
        }
        if (fileUrl != null) {
            member.setPhotoUrl(fileUrl);
        }
        if (request.getPhone().length() != 0) {
            checkPhone(request.getPhone());

            member.setPhone(request.getPhone());
        }
        if (request.getCity().length() != 0 && request.getStreet().length() != 0
                && request.getZipcode().length() != 0) {
            Address address = new Address(request.getCity(), request.getStreet(),
                    request.getZipcode());

            member.setAddress(address);
        }
    }

    // 회원 탈퇴
    /**
     * ㄴ탈퇴
     * => 회원 테이블에 해당 회원 정보 확인하여 삭제
     */
    @Override
    @Transactional
    public void delete(Long no) {
        if (myInfo(no) != null) {
            memberRepositoryJPA.deleteById(no);
        } else {
            throw new IllegalStateException("회원을 찾을 수 없습니다.");
        }
    }

    @Override
    public List<Member> myInfoTest(Long memberNo) {
        return memberRepository.findOneByNo(memberNo);
    }

}