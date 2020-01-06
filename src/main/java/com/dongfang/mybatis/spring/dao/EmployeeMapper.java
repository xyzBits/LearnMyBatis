package com.dongfang.mybatis.spring.dao;

import com.dongfang.mybatis.spring.bean.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmployeeMapper {
    Employee getEmployeeById(@Param("id") Integer id);

    List<Employee> getAllEmps();
}
