package com.deili.deilimanagement.user.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // e.g., "IT", "Design", "Accounting"

    @OneToMany(mappedBy = "department")
    private List<JobRole> roles;
}
