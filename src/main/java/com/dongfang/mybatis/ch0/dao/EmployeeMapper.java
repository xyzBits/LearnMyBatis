package com.dongfang.mybatis.ch0.dao;

import com.dongfang.mybatis.ch0.bean.Employee;

public interface EmployeeMapper {
    Employee getEmpById(Integer id);
}
