package com.dongfang.mybatis.dao;

import com.dongfang.mybatis.bean.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自动映射：
 *      1、全局setting设置
 *              autoMappingBehavior默认是Partial，开户自动映射的功能，唯一的要求是列名和javaBean属性名一致
 *              如果autoMappingBehavior设置为null则会取消自动映射
 *              数据库字段命名规范，pojo属性符合驼峰命名法，如A_COLUMN->aColumn，我们可以开启自动驼峰命名规则映射功能，
 *                  mapUnderscoreToCamelCase = true
 *      2、自定义resultMap，实现高级结果集映射
 */

public interface ResultAutoMapper {
    Employee getEmpById(@Param("id") Integer id);

    Employee getEmpAndDeptById(@Param("id") Integer id);

    Employee getEmpAndDeptByAssociation(@Param("id") Integer id);

    Employee getEmpStepByStep(@Param("id") Integer id);

    List<Employee> getAllEmpByDeptId(@Param("deptId") Integer deptId);
}
