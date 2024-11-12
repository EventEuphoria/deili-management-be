package com.deili.deilimanagement.user.service;

import com.deili.deilimanagement.user.entity.Department;
import com.deili.deilimanagement.user.entity.JobRole;

import java.util.List;

public interface RoleService {
    Department createDepartment(String name);
    JobRole createRole(String title, Long departmentId);
    List<Department> getAllDepartments();
    List<JobRole> getAllRoles();
    Department getDepartmentById(Long id);
    JobRole getRoleById(Long id);
    Department updateDepartment(Long id, String name);
    JobRole updateRole(Long id, String title, Long departmentId);
    void deleteDepartment(Long id);
    void deleteRole(Long id);
}
