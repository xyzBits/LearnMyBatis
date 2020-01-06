package com.dongfang.mybatis.spring.service;

import com.dongfang.mybatis.spring.bean.Employee;
import com.dongfang.mybatis.spring.dao.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;


    public List<Employee> getAllEmps() {
        return employeeMapper.getAllEmps();
    }
}
