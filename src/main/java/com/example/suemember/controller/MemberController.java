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
@RequestMapping("/") //복수로 네이밍
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
    // responseEntity 사용 방법 확인
    public ResponseEntity<Member> login(@RequestBody Member member, HttpServletRequest request) {

        Member loginMember = memberService.loginMember(member.getEmail(), member.getPassword());

        // 세션
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("loginMember", loginMember); // db나 spring session에 담아서 로그아웃 할 떄 검사

        // 로그인 시에 ok라는 토큰을 담아서 나머지 기능 시에 비교하면서 기능을 구현해야함
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



    @PatchMapping("/members/{id}") // update, delete 주소가 필요 없음 , serivce에서 return
    public ResponseEntity<String> updateMemeber(@RequestHeader("Test-Header") String token,
                                                @RequestBody Member member, @PathVariable("id") Long id,
                                                HttpServletRequest request) {

        HttpSession httpSession = request.getSession(false);

        if (token.equals("ok") && httpSession != null) {
            // 회원 정보를 불러서 비교한 뒤에 유효하면 회원 수정이 되게끔 유효성 체크 ( 롤백, 트랜잭션, 세션이 유효하지 않을 시)
          Member updateMember =  memberService.updateMember(id, member);
            httpSession.invalidate();

            return ResponseEntity.status(HttpStatus.OK).body(updateMember.toString()); // return 값 수정

        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("update fail"); // resault status를 물고다님
    }

    // controller에서는 최소한의 처리만 함 session이나 토큰은 service 단으로 다 넘어가게 해서 service에서 예외처리
    @DeleteMapping("/members/{id}")
    public ResponseEntity<String> deleteMember(@RequestHeader("Test-Header") String token,
                                              @PathVariable("id") Long id,
                                              HttpServletRequest request) {

        HttpSession httpSession = request.getSession(false);

        if (token.equals("ok") && httpSession != null) {
            // id 정책이 맞는지, id가 있는지 우선 확인 후에 삭제 진행  -> 중복 작업이 생기면 따로 service로 빼서 구현
            memberService.deleteMember(id);
            httpSession.invalidate();

            // 지운 결과 return 받아서 확인 받기
            return ResponseEntity.status(HttpStatus.OK).body("bye bye!");

        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원 탈퇴 실패");
    }
}