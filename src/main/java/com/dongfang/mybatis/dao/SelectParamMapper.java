package com.dongfang.mybatis.dao;

import com.dongfang.mybatis.bean.Employee;

public interface SelectParamMapper {
    Employee getEmpById(Integer id);

    void addEmp(Employee emp);

    void updateEmp(Employee emp);

    void deleteEmpById(Integer id);
}
