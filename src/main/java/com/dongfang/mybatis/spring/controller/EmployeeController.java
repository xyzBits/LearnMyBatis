package com.dongfang.mybatis.spring.controller;

import com.dongfang.mybatis.spring.bean.Employee;
import com.dongfang.mybatis.spring.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping("/getEmps")
    public String getEmps(Map<String, Object> map) {
        List<Employee> allEmps = employeeService.getAllEmps();
        allEmps.stream().forEach(System.out::println);
        map.put("allEmps", allEmps);
        return "list";
    }
}
