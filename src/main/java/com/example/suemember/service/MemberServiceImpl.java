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


@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;
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
    public TokenResponse loginMember(String email, String password) {


        Member member = memberRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 옳바르지 않습니다."));
        Auth auth = authRepository.findById(member.getId())
                .orElseThrow(() -> new IllegalArgumentException("Token 이 존재하지 않습니다."));

        String accessToken;
        String refreshToken;   //DB에서 가져온 Refresh 토큰

        //둘 다 새로 발급
        accessToken = jwtTokenProvider.createAccessToken(member.getEmail());
        refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail());
        auth.refreshUpdate(refreshToken);   //DB Refresh 토큰 갱신


        return TokenResponse.builder()
                .ACCESS_TOKEN(accessToken)
                .REFRESH_TOKEN(refreshToken)
                .build();
    }

    @Override
    public TokenResponse updateMember(Long id, Member memberRequest) {
        System.out.println(id);
        Member updateMember = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원찾기 실패"));

        Auth auth = authRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Token 이 존재하지 않습니다."));

        String accessToken = auth.getAccessToken();
        log.info("accessToken01"+ accessToken);
        String refreshToken = auth.getRefreshToken();
        log.info("refreshToken"+ refreshToken);

        if (jwtTokenProvider.isValidRefreshToken(refreshToken)) {
            accessToken = jwtTokenProvider.createAccessToken(updateMember.getEmail()); //Access Token 새로 만들어서 줌
            log.info("accessToken_if문 " + accessToken);
            Member member = Member.builder()
                    .id(updateMember.getId())
                    .email(updateMember.getEmail())
                    .memberName(memberRequest.getMemberName())
                    .password(memberRequest.getPassword())
                    .age(memberRequest.getAge())
                    .role(updateMember.getRole())
                    .build();

            System.out.println(member.toString());

            memberRepository.save(member);

            log.info(member.toString());

            return TokenResponse.builder()
                    .ACCESS_TOKEN(accessToken)
                    .REFRESH_TOKEN(refreshToken)
                    .build();
        }

        log.info("accessToken02"+ accessToken);
        return TokenResponse.builder()
                .ACCESS_TOKEN(accessToken)
                .REFRESH_TOKEN(refreshToken)
                .build();
    }


    @Override
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);

    }
}
