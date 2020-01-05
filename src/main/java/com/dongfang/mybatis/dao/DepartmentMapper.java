package com.dongfang.mybatis.dao;

import com.dongfang.mybatis.bean.Department;
import org.apache.ibatis.annotations.Param;

public interface DepartmentMapper {
    Department getDeptById(@Param("id") Integer id);

    Department getDeptWithEmpById(@Param("id") Integer id);

    Department getDeptWithEmpStepById(@Param("id") Integer id);
}
