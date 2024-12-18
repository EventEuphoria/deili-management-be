package com.deili.deilimanagement.card.entity;

import com.deili.deilimanagement.lane.entity.Lane;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "card")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardName;

    private String cardDesc;

    private LocalDateTime dueDate;

    private int position;

    @ManyToOne
    @JoinColumn(name = "lane_id", nullable = false)
    @JsonBackReference
    private Lane lane;

    @OneToOne(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Label label;

    @OneToOne(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Checklist checklist;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CardAssignee> cardAssignees;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}