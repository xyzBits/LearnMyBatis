package com.dongfang.mybatis.ch0;

import com.dongfang.mybatis.ch0.bean.Employee;
import com.dongfang.mybatis.ch0.dao.EmployeeCrudMapper;
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


    @Test
    public void testInsert() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        // 获取到的sqlSession不会自动提交
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            EmployeeCrudMapper crudMapper = sqlSession.getMapper(EmployeeCrudMapper.class);
            Employee employee = new Employee("Jerry", "Jerry@gmail.com", "M");
            crudMapper.addEmp(employee);
            sqlSession.commit(); //不提交的话，数据库不会出现
        } finally {
            sqlSession.close();
        }
    }
}
