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

        Auth authSaveRefresh = Auth.builder()
                .email(member.getEmail())
                .refreshToken(refreshToken)
                .build();

        authRepository.save(authSaveRefresh);

        return TokenResponse.builder()
                .ACCESS_TOKEN(accessToken)
                .REFRESH_TOKEN(refreshToken)
                .build();
    }

    @Override
    public TokenResponse loginMember(String email, String password) {

        Member member = memberRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 옳바르지 않습니다."));

        Auth auth = authRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Token이 존재하지 않습니다."));

        //둘 다 새로 발급
        String accessToken = jwtTokenProvider.createAccessToken(member.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail());

        Auth authSaveRefresh = Auth.builder()
                .authId(auth.getAuthId())
                .email(member.getEmail())
                .refreshToken(refreshToken)
                .build();

        authRepository.save(authSaveRefresh);

        return TokenResponse.builder()
                .ACCESS_TOKEN(accessToken)
                .REFRESH_TOKEN(refreshToken)
                .build();
    }

    @Override
    public TokenResponse updateMember(Long id, Member memberRequest) {

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원 찾기 실패"));

        Auth auth = authRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Token이 존재하지 않습니다."));

        String accessToken = "";
        String refreshToken = auth.getRefreshToken();

        if (jwtTokenProvider.isValidRefreshToken(refreshToken)) {
            accessToken = jwtTokenProvider.createAccessToken(member.getEmail()); //Access Token 새로 만들어서 줌

            Member updateMember = Member.builder()
                    .id(member.getId())
                    .email(member.getEmail())
                    .memberName(memberRequest.getMemberName())
                    .password(memberRequest.getPassword())
                    .age(memberRequest.getAge())
                    .role(member.getRole())
                    .build();

            memberRepository.save(updateMember);

            return TokenResponse.builder()
                    .ACCESS_TOKEN(accessToken)
                    .REFRESH_TOKEN(refreshToken)
                    .build();
        }

        // 여기를 어떻게 처리할지 고민하기
        return TokenResponse.builder()
                .ACCESS_TOKEN(accessToken)
                .REFRESH_TOKEN(refreshToken)
                .build();
    }

    @Override
    public TokenResponse logoutMember(Long id, String refreshToken) {

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원 찾기 실패"));

        Auth auth = authRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Token이 존재하지 않습니다."));

        if(refreshToken.equals(auth.getRefreshToken())){

            return TokenResponse.builder()
                    .ACCESS_TOKEN("logout success")
                    .REFRESH_TOKEN("logout success")
                    .build();
        }

        return TokenResponse.builder()
                .ACCESS_TOKEN("failed")
                .REFRESH_TOKEN("failed")
                .build();
    }

    @Override
    public String deleteMember(Long id, String refreshToken) {

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원 찾기 실패"));

        Auth auth = authRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Token이 존재하지 않습니다."));


        if (jwtTokenProvider.isValidRefreshToken(refreshToken) && refreshToken.equals(auth.getRefreshToken())) {

            authRepository.deleteById(auth.getAuthId());
            memberRepository.deleteById(id);
            return "bye!";
        }

        return "failed";
    }


}
