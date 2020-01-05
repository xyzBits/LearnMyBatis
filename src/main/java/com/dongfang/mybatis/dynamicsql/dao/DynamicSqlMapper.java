package com.dongfang.mybatis.dynamicsql.dao;

import com.dongfang.mybatis.bean.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DynamicSqlMapper {
    //携带了哪个字段，查询条件就带上这个节段的值
    List<Employee> getEmployeesByCondition(@Param("employee") Employee employee);

    List<Employee> getEmployeesByConditionTrim(@Param("employee") Employee employee);

    List<Employee> getEmployeesByConditionChoose(@Param("employee") Employee employee);

    void updateEmployee(@Param("employee") Employee employee);

    List<Employee> getEmployeesByConditionForeach(@Param("ids") List<Integer> ids);

    void addEmployee(@Param("employees") List<Employee> employees);

    List<Employee> getEmployeesByInnerParameter(@Param("employee") Employee employee);
}
