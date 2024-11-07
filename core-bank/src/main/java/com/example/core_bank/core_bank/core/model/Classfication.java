package com.example.core_bank.core_bank.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "classfication")
@Getter
@NoArgsConstructor
public class Classfication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "classfication_id")
    private Integer id;

    @Column(name = "classfication_name", nullable = false,length = 50)
    private String classficationName;

    private Classfication(String classficationName) {
        this.classficationName = classficationName;
    }

    public static Classfication createClassfication(String classficationName) {
        return new Classfication(classficationName);
    }
}
