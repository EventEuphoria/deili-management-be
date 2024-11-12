package com.deili.deilimanagement.user.controller;

import com.deili.deilimanagement.user.entity.Department;
import com.deili.deilimanagement.user.entity.JobRole;
import com.deili.deilimanagement.user.service.RoleService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @QueryMapping
    public List<Department> getAllDepartments() {
        return roleService.getAllDepartments();
    }

    @QueryMapping
    public Department getDepartmentById(@Argument Long id) {
        return roleService.getDepartmentById(id);
    }

    @MutationMapping
    public Department createDepartment(@Argument String name) {
        return roleService.createDepartment(name);
    }

    @MutationMapping
    public Department updateDepartment(@Argument Long id, @Argument String name) {
        return roleService.updateDepartment(id, name);
    }

    @MutationMapping
    public String deleteDepartment(@Argument Long id) {
        roleService.deleteDepartment(id);
        return "Department deleted successfully.";
    }

    @QueryMapping
    public List<JobRole> getAllRoles() {
        return roleService.getAllRoles();
    }

    @QueryMapping
    public JobRole getRoleById(@Argument Long id) {
        return roleService.getRoleById(id);
    }

    @MutationMapping
    public JobRole createJobRole(@Argument String title, @Argument Long departmentId) {
        return roleService.createRole(title, departmentId);
    }

    @MutationMapping
    public JobRole updateJobRole(@Argument Long id, @Argument String title, @Argument Long departmentId) {
        return roleService.updateRole(id, title, departmentId);
    }

    @MutationMapping
    public String deleteJobRole(@Argument Long id) {
        roleService.deleteRole(id);
        return "Job Role deleted successfully.";
    }
}
