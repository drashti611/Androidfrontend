package com.example.androidfrontend.Model;

import java.util.List;

public class DepartmentResponse {
    private boolean success;
    private List<Department> department;

    public boolean isSuccess() {
        return success;
    }

    public List<Department> getDepartment() {
        return department;
    }
}

class Department {
    private String deptName;

    public String getDeptName() {
        return deptName;
    }
}
