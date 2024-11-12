package com.deili.deilimanagement.user.service.Impl;

import com.deili.deilimanagement.user.entity.Department;
import com.deili.deilimanagement.user.entity.JobRole;
import com.deili.deilimanagement.user.repository.DepartmentRepository;
import com.deili.deilimanagement.user.repository.JobRoleRepository;
import com.deili.deilimanagement.user.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final DepartmentRepository departmentRepository;
    private final JobRoleRepository jobRoleRepository;

    public RoleServiceImpl(DepartmentRepository departmentRepository, JobRoleRepository jobRoleRepository) {
        this.departmentRepository = departmentRepository;
        this.jobRoleRepository = jobRoleRepository;
    }

    @Override
    public Department createDepartment(String name) {
        Department department = new Department();
        department.setName(name);
        return departmentRepository.save(department);
    }

    @Override
    public JobRole createRole(String title, Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));
        JobRole jobRole = new JobRole();
        jobRole.setTitle(title);
        jobRole.setDepartment(department);
        return jobRoleRepository.save(jobRole);
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public List<JobRole> getAllRoles() {
        return jobRoleRepository.findAll();
    }

    @Override
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));
    }

    @Override
    public JobRole getRoleById(Long id) {
        return jobRoleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("JobRole not found"));
    }

    @Override
    public Department updateDepartment(Long id, String name) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));
        department.setName(name);
        return departmentRepository.save(department);
    }

    @Override
    public JobRole updateRole(Long id, String title, Long departmentId) {
        JobRole jobRole = jobRoleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("JobRole not found"));
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));
        jobRole.setTitle(title);
        jobRole.setDepartment(department);
        return jobRoleRepository.save(jobRole);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));
        departmentRepository.delete(department);
    }

    @Override
    public void deleteRole(Long id) {
        JobRole jobRole = jobRoleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("JobRole not found"));
        jobRoleRepository.delete(jobRole);
    }
}