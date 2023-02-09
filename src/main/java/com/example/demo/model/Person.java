package com.example.demo.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="PERSONS")
public class Person {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;
}
