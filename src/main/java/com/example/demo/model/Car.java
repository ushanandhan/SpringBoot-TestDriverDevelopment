package com.example.demo.model;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@Data
@Entity
@Table(name="CARS")
public class Car {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column
    private String type;

    @OneToOne
    @JoinColumn(name = "person_id")
    private Person person;

    public Car() {}

    public Car(String name, String type) {
        this.name = name;
        this.type = type;
    }



}