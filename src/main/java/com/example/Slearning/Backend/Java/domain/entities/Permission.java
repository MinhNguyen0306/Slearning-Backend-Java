package com.example.Slearning.Backend.Java.domain.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "permission")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id", nullable = false)
    private Integer id;

    @Column(name = "permission_title")
    private String permission;

    @ManyToMany(mappedBy = "permissions")
    private List<Role> roles;
}
