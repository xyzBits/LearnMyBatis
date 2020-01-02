package com.dongfang.mybatis.dao;

import com.dongfang.mybatis.bean.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SelectParamMapper {
    Employee getEmpById(Integer id);

    Employee getEmpByIdAndLastName(@Param("id") Integer id, @Param("lastName") String lastName);

    Employee getEmpByMap(Map<String, Object> map);

    Employee getEmpByIdAntOtherEmp(@Param("id") Integer id, @Param("employee") Employee employee);

    Employee getEmpByIds(List<Integer> ids);

    Employee getEmpByTableAndId(@Param("tableName") String tableName, @Param("id") Integer id);

    void addEmp(Employee emp);

    void updateEmp(Employee emp);

    void deleteEmpById(Integer id);
}
