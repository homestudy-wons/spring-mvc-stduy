package com.example.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Member {
    @Id
    @Column(length = 20, nullable = false)
    private String id;

    @Column(length = 20, nullable = false)
    private String password;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 10, nullable = false)
    private String status;

    @Builder
    public Member(String id, String password, String name) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.status = "join";
    }

    public void withdraw() {
        this.status = "withdraw";
    }

}
