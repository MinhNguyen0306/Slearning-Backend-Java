package com.example.Slearning.Backend.Java.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "permission")
public class Permission extends BaseEntity {
    @Column(name = "permission_title")
    private String permission;

    @ManyToMany(mappedBy = "permissions")
    private List<Role> roles;
}
