package com.dongfang.mybatis.ch0.dao;

import com.dongfang.mybatis.ch0.bean.Employee;

public interface EmployeeCrudMapper {
    Employee getEmpById(Integer id);

    void addEmp(Employee emp);

    void updateEmp(Employee emp);

    void deleteEmpById(Integer id);
}
