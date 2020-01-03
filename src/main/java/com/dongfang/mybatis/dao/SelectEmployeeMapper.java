package com.dongfang.mybatis.dao;

import com.dongfang.mybatis.bean.Employee;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SelectEmployeeMapper {
    List<Employee> getEmpsByLastNameLike(@Param("lastName") String lastName);

    // 返回一条记录的map： key是列名，值就是对应的值
    // empMap = {gender=M, last_name=Dong fang, id=1, email=Jerry@gmail.com}
    Map<String, Object> getEmpMapById(@Param("id") Integer id);

    // 多条记录封装成一个map，Map<Integer, Employee> key 主键，值，封装后的
    // 告诉mybatis封装map的时候用哪个属性作为map的主键
    @MapKey(value = "id")
    Map<Integer, Employee> getAllEmpByLastNameLike(@Param("lastName") String lastName);
}
