package com.deili.deilimanagement.card.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "label_item")
public class LabelItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "label_id", nullable = false)
    private Label label;

    private String labelName;
}
