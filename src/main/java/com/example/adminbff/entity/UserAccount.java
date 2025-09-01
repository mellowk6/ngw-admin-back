package com.example.adminbff.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String username;      // 사번

    @Column(nullable = false)
    private String passwordHash;  // BCrypt

    @Column(nullable = false, length = 100)
    private String displayName;

    @Column(nullable = false, length = 50)
    private String deptCode;

    @Column(nullable = false, length = 100)
    private String company;

    @Column(nullable = false, length = 100)
    private String roles;         // 예: "ROLE_USER" (CSV로 간단히)
}