package com.example.suemember.service;
import com.example.suemember.domain.entity.Member;
import com.example.suemember.dto.TokenResponse;

import java.util.List;

public interface MemberService {

    List<Member> getAllMember ();
    TokenResponse addNewMember (Member member);
    Member loginMember(String email, String password);
    Member updateMember (Long id, Member member);
    void deleteMember (Long id);

}
