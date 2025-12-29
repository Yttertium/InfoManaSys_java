package com.ynu.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "personnel")
public class Personnel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String gender;
    private Integer age;
    private String position;
    private String phone;
    private String email;
}
