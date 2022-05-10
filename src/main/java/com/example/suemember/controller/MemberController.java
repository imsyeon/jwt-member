package com.example.suemember.controller;

import com.example.suemember.domain.entity.Member;
import com.example.suemember.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;


@RestController
@RequestMapping("/")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {

        this.memberService = memberService;

    }

    // 모든 회원 정보
    @GetMapping("/members")
    public List<Member> getAllMember() {

        return memberService.getAllMember();
    }

    // 회원가입
    @PostMapping("/member")
    public Member addNewMember(@RequestBody Member member) {

        member = memberService.addNewMember(member);

        return member;
    }

    @PostMapping("/login")
    public ResponseEntity<Member> login(@RequestBody Member member, HttpServletRequest request) {

        Member loginMember = memberService.loginMember(member.getEmail(), member.getPassword());

        // 세션
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("loginMember", loginMember);


        return ResponseEntity.status(HttpStatus.OK).body(loginMember);
    }


    //Test-Header값 임의로 지정해서 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Test-Header") String token, HttpServletRequest request) {

        HttpSession httpSession = request.getSession(false);

        if (token.equals("ok") && httpSession != null) {

            httpSession.invalidate();

            return ResponseEntity.status(HttpStatus.OK).body("logout");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("fail");
    }


    @PatchMapping("/members/{id}")
    public ResponseEntity<String> updateMemeber(@RequestHeader("Test-Header") String token,
                                                @RequestBody Member member, @PathVariable("id") Long id,
                                                HttpServletRequest request) {

        HttpSession httpSession = request.getSession(false);

        if (token.equals("ok") && httpSession != null) {

          Member updateMember =  memberService.updateMember(id, member);
            httpSession.invalidate();

            return ResponseEntity.status(HttpStatus.OK).body(updateMember.toString());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("update fail");
    }


    @DeleteMapping("/members/{id}")
    public ResponseEntity<String> deleteMember(@RequestHeader("Test-Header") String token,
                                              @PathVariable("id") Long id,
                                              HttpServletRequest request) {

        HttpSession httpSession = request.getSession(false);

        if (token.equals("ok") && httpSession != null) {

            memberService.deleteMember(id);
            httpSession.invalidate();

            return ResponseEntity.status(HttpStatus.OK).body("bye bye!");

        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원 탈퇴 실패");
    }
}