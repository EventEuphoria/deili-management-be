package com.deili.deilimanagement.card.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "checklist_item")
public class ChecklistItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "checklist_id", nullable = false)
    private Checklist checklist;

    private String content;

    private boolean status;
}