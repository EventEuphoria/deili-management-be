package com.deili.deilimanagement.card.repository;

import com.deili.deilimanagement.card.entity.CardAssignee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardAssigneeRepository extends JpaRepository<CardAssignee, Long> {
}
