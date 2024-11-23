package com.deili.deilimanagement.user.repository;

import com.deili.deilimanagement.user.entity.Department;
import com.deili.deilimanagement.user.entity.JobRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRoleRepository extends JpaRepository<JobRole, Long> {
    Optional<JobRole> findByTitle(String title);
    List<JobRole> findAllByDepartment(Department department);
}
