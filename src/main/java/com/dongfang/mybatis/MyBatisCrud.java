package com.dongfang.mybatis;

import com.dongfang.mybatis.bean.Employee;
import com.dongfang.mybatis.dao.EmployeeCrudMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class MyBatisCrud {
    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "MyBatis/mybatis-conf.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

    /**
     * 使用MyBatis测试增删改
     * 1、MyBatis允许增删改直接定义以下类型的返回值
     *          Integer Long Boolean 包装类型和原始类型
     *          返回影响的行数，大于0为true
     * 2、需要手动提交数据
     *          sqlSessionFactory.openSession() 这样打开会话需要手动提交数据
     *          sqlSessionFactory.openSession(true) 这样就是自动提交数据
     */

    @Test
    public void testInsert() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        // 获取到的sqlSession不会自动提交

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            EmployeeCrudMapper crudMapper = sqlSession.getMapper(EmployeeCrudMapper.class);
            Employee employee = new Employee(null, "Jerry", "Jerry@gmail.com", "M");
            crudMapper.addEmp(employee);
            sqlSession.commit(); //不提交的话，数据库不会出现
        }
    }

    @Test
    public void testGetAutoIncrementPrimaryKey() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            EmployeeCrudMapper crudMapper = sqlSession.getMapper(EmployeeCrudMapper.class);
            Employee employee = new Employee(null, "Jerry", "Jerry@gmail.com", "M");
            System.out.println("employee.getId() = " + employee.getId());
            crudMapper.addEmp(employee);
            System.out.println("employee.getId() = " + employee.getId());
        }
    }

    @Test
    public void testUpdate() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            EmployeeCrudMapper crudMapper = sqlSession.getMapper(EmployeeCrudMapper.class);
            Employee employee = new Employee(1, "Dong fang", "Jerry@gmail.com", "M");
            long affectedNum = crudMapper.updateEmp(employee);
            System.out.println("affectedNum = " + affectedNum);
            // 可以自动提交数据
        }
    }

    @Test
    public void testDelete() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            EmployeeCrudMapper mapper = sqlSession.getMapper(EmployeeCrudMapper.class);
            mapper.deleteEmpById(5);
            sqlSession.commit();
        }
    }
}
