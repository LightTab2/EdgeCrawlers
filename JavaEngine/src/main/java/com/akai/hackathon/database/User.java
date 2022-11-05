package com.akai.hackathon.database;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Table(name = "users")
public class User {

    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "category")
    private String category;

    public User(String name, String password, String category) {
        this.name = name;
        this.password = password;
        this.category = category;
    }
}
