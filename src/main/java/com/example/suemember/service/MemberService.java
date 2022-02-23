package com.example.suemember.service;

import com.example.suemember.domain.entity.Member;

import java.util.List;

public interface MemberService {
//    public UserVO getUser(UserVO vo);
//    void insertUser(UserVO vo);
//    void deleteUser(UserVO vo);
//    void modifyUser(UserVO vo);

    List<Member> getAllMember ();
    Member addNewMember (Member member);
    Member getMember (Long id);
    Member updateMember (Long id, Member member);
    void deleteMember (Long id);

}
