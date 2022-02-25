package com.example.suemember.service;

import com.example.suemember.domain.entity.Member;
import com.example.suemember.domain.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService{

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public List<Member> getAllMember() {

        return memberRepository.findAll();
    }

    @Override
    public Member addNewMember(Member member) {

        return memberRepository.save(member);
    }

    @Override
    public Member loginMember(String email, String password) {

        Optional<Member> loginMember = memberRepository.findByEmailAndPassword(email,password);

        if (!loginMember.isPresent()){
            throw new IllegalArgumentException("잘못된 로그인 정보입니다.");
        }

        return loginMember.get();
    }

    @Override
    public Member updateMember(Long id, Member member) {

        Member updateMember = memberRepository.findById(id).orElseThrow(() ->{
            return new IllegalArgumentException("회원찾기 실패") ;
        });

        updateMember.setMemberName(member.getMemberName());
        updateMember.setPassword(member.getPassword());

        System.out.println(updateMember);

        return memberRepository.save(updateMember);
    }


    @Override
    public void deleteMember(Long id) {

    }
}
