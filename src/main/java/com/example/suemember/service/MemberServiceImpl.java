package com.example.suemember.service;

import com.example.suemember.domain.entity.Auth;
import com.example.suemember.domain.entity.Member;
import com.example.suemember.domain.repository.AuthRepository;
import com.example.suemember.domain.repository.MemberRepository;
import com.example.suemember.dto.TokenResponse;
import com.example.suemember.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;
    //private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberServiceImpl(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository, AuthRepository authRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
        this.authRepository = authRepository;
    }

    @Override
    public List<Member> getAllMember() {

        return memberRepository.findAll();
    }

    @Override
    public TokenResponse addNewMember(Member memberRequest) {
        // 중복체크, 공백 체크, 이메일 유효성 확인
        log.info(memberRequest.getAge());

        Member member = Member.builder()
                .memberName(memberRequest.getMemberName())
                .email(memberRequest.getEmail())
                .password(memberRequest.getPassword())
                .age(memberRequest.getAge())
                .role(memberRequest.getRole())
                .build();
        memberRepository.save(member);

        String accessToken = jwtTokenProvider.createAccessToken(member.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail());
        Auth auth = Auth.builder()
                .member(member)
                .refreshToken(refreshToken)
                .build();
        authRepository.save(auth);
        //토큰들을 반환한 순간 로그인 처리가 된 것임
        return TokenResponse.builder()
                .ACCESS_TOKEN(accessToken)
                .REFRESH_TOKEN(refreshToken)
                .build();
    }

    @Override
    public Member loginMember(String email, String password) {

        Optional<Member> loginMember = memberRepository.findByEmailAndPassword(email, password);

        if (loginMember.isPresent()) {

           return loginMember.get();
        }

        throw new IllegalArgumentException("잘못된 로그인 정보입니다.");
    }

    @Override
    public Member updateMember(Long id, Member member) {

        Member updateMember = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("회원찾기 실패"));

        updateMember.setMemberName(member.getMemberName());
        updateMember.setPassword(member.getPassword());

        return memberRepository.save(updateMember);
    }


    @Override
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);

    }
}
