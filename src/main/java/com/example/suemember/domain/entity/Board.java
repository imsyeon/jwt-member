package com.example.suemember.domain.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Setter
@Getter
@Entity
@ToString
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue
    private Long seq;

    private String title;

    private String content;

    @Column(length = 10, nullable = false, updatable = false)
    private String writer;
    @Column(name = "createDate", insertable = false, updatable = false, columnDefinition = "date default sysdate")
    private Date createDate;


}
