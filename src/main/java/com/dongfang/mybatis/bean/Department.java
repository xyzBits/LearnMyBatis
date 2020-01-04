package com.dongfang.mybatis.bean;

import java.util.List;

public class Department {
    private Integer id;
    private String departmentName;
    private List<Employee> employees;


    public Department() {
    }

    public Department(Integer id, String departmentName) {
        this.id = id;
        this.departmentName = departmentName;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"departmentName\":\"")
                .append(departmentName).append('\"');
        sb.append(",\"employees\":")
                .append(employees);
        sb.append('}');
        return sb.toString();
    }
}
