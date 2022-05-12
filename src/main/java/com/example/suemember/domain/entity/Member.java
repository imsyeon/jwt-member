package com.example.suemember.domain.entity;
import lombok.*;

import javax.persistence.*;

@Setter @Getter
@Entity
@Builder
@AllArgsConstructor
@ToString
// 기본생성자 생성
@NoArgsConstructor
public class Member {

    @Id
    // SQL 에서 자동생성되도록 돕는 어노테이션
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String memberName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    private String age;

    @Column(nullable = false)
    private String role;
}
