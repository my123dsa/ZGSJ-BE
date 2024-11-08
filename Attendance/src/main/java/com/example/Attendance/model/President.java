package com.example.Attendance.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "president")
@Getter
@NoArgsConstructor
public class President {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "president_id")
    private Integer id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

}