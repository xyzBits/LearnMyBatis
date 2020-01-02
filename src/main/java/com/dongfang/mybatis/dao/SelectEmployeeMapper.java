package com.dongfang.mybatis.dao;

import com.dongfang.mybatis.bean.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SelectEmployeeMapper {
    List<Employee> getEmpsByLastNameLike(@Param("lastName") String lastName);
}
