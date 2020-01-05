package com.dongfang.mybatis.dynamicsql;

import com.dongfang.mybatis.bean.Employee;
import com.dongfang.mybatis.dynamicsql.dao.DynamicSqlMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyBatisDynamicSql {
    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "MyBatis/mybatis-conf.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

    @Test
    public void testIfSql() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            DynamicSqlMapper mapper = sqlSession.getMapper(DynamicSqlMapper.class);
            Employee employee = new Employee();
//            employee.setId(1);
            employee.setLastName("Dong fang");
            employee.setEmail("Jerry@gmail.com ");
            // 查询 的时候，如果第一个条件不带， where 后为and
            // SQL: select *   from t_employee  where  and last_name = ?   and email = ?
            // where 后面加1 = 1
            // select * from t_employee where 1 = 1 and last_name = ? and email = ?
            List<Employee> employeesByCondition = mapper.getEmployeesByCondition(employee);
            System.out.println("employeesByCondition = " + employeesByCondition);
        }
    }

    @Test
    public void testTrimAnd() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            DynamicSqlMapper mapper = sqlSession.getMapper(DynamicSqlMapper.class);
            Employee employee = new Employee();
//            employee.setId(1);
            employee.setLastName("Dong fang");
            employee.setEmail("Jerry@gmail.com ");

            List<Employee> employeesByCondition = mapper.getEmployeesByConditionTrim(employee);
            System.out.println("employeesByCondition = " + employeesByCondition);
        }
    }

    @Test
    public void testChooseSelect() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            DynamicSqlMapper mapper = sqlSession.getMapper(DynamicSqlMapper.class);
            Employee employee = new Employee();
//            employee.setId(1);
//            employee.setLastName("Dong fang");
//            employee.setEmail("Jerry@gmail.com ");

            List<Employee> employeesByCondition = mapper.getEmployeesByConditionChoose(employee);
            System.out.println("employeesByCondition = " + employeesByCondition);
        }
    }

    @Test
    public void testUpdateSet() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            DynamicSqlMapper mapper = sqlSession.getMapper(DynamicSqlMapper.class);
            Employee employee = new Employee();
            employee.setId(1);
            employee.setLastName("Dong fang");
            employee.setEmail("dongfang@gmail.com ");

            mapper.updateEmployee(employee);
        }
    }

    @Test
    public void testForeachSelect() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            DynamicSqlMapper mapper = sqlSession.getMapper(DynamicSqlMapper.class);
            List<Employee> employeesByConditionForeach = mapper.getEmployeesByConditionForeach(Arrays.asList(1, 2, 6));
            System.out.println("employeesByConditionForeach = " + employeesByConditionForeach);
        }
    }

    @Test
    public void testForeachInsert() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            DynamicSqlMapper mapper = sqlSession.getMapper(DynamicSqlMapper.class);
            List<Employee> employees = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                employees.add(new Employee("lastName-" + i, "lastName-" + i + "@gmail.com", i % 2 + "", i % 2 == 0 ? 1 : 2));
            }
            mapper.addEmployee(employees);
        }
    }

    @Test
    public void testInnerParameter() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            DynamicSqlMapper mapper = sqlSession.getMapper(DynamicSqlMapper.class);
            Employee employee = new Employee();
            employee.setLastName("o");
            List<Employee> employees = mapper.getEmployeesByInnerParameter(employee);
            employees.stream().forEach(System.out::println);
        }
    }
}
