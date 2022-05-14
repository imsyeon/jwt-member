package com.example.suemember.service;
import com.example.suemember.domain.entity.Member;
import com.example.suemember.dto.TokenResponse;

import java.util.List;

public interface MemberService {

    List<Member> getAllMember ();
    TokenResponse addNewMember (Member member);
    TokenResponse loginMember(String email, String password);
    TokenResponse updateMember (Long id, Member member);
    Boolean deleteMember (Long id, String refreshToken);
    Boolean logoutMember(Long id, String refreshToken);
}
