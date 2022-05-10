package com.example.suemember.service;

import com.example.suemember.domain.entity.Member;
import com.example.suemember.domain.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public List<Member> getAllMember() {

        return memberRepository.findAll();
    }

    @Override
    public Member addNewMember(Member member) {
        // 중복체크, 공백 체크, 이메일 유효성 확인
        log.info(member.getAge());

        return memberRepository.save(member);
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

        Member updateMember = memberRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("회원찾기 실패");
        });

        updateMember.setMemberName(member.getMemberName());
        updateMember.setPassword(member.getPassword());

        return memberRepository.save(updateMember);
    }


    @Override
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);

    }
}
