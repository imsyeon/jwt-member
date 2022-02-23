package com.example.suemember.service;

import com.example.suemember.domain.entity.Member;
import com.example.suemember.domain.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Member getMember(Long id) {
        return null;
    }

    @Override
    public Member updateMember(Long id, Member member) {
        return null;
    }

    @Override
    public void deleteMember(Long id) {

    }
}
