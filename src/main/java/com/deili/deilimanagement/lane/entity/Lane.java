package com.deili.deilimanagement.lane.entity;

import com.deili.deilimanagement.board.entity.Board;
import com.deili.deilimanagement.card.entity.Card;
import com.deili.deilimanagement.user.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "lane")

public class Lane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String laneName;

    @OneToMany(mappedBy = "lane", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Card> card;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    @JsonBackReference
    private Board board;

    private int position;

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
