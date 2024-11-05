package com.example.core_bank.core_bank.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "classfication")
@Getter
@NoArgsConstructor
public class Classfication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "classfication_id")
    private int id;

    @Column(name = "classfication_name", nullable = false)
    private LocalDate classficationName;
}
