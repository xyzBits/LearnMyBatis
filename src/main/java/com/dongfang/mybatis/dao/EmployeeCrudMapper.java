package com.dongfang.mybatis.dao;

import com.dongfang.mybatis.bean.Employee;

public interface EmployeeCrudMapper {
    Employee getEmpById(Integer id);

    void addEmp(Employee emp);

    long updateEmp(Employee emp);

    void deleteEmpById(Integer id);
}
