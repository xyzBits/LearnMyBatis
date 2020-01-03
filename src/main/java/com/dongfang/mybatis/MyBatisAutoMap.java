package com.dongfang.mybatis;

import com.dongfang.mybatis.bean.Employee;
import com.dongfang.mybatis.dao.ResultAutoMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import javax.swing.plaf.SliderUI;
import java.io.IOException;
import java.io.InputStream;

public class MyBatisAutoMap {
    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "MyBatis/mybatis-conf.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

    @Test
    public void testResultMap() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            ResultAutoMapper mapper = sqlSession.getMapper(ResultAutoMapper.class);
            Employee employee = mapper.getEmpById(1);
            System.out.println("employee = " + employee);
        }
    }

    @Test
    public void testJoinSelect() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            ResultAutoMapper mapper = sqlSession.getMapper(ResultAutoMapper.class);
            Employee employee = mapper.getEmpAndDeptById(1);
            System.out.println("employee = " + employee);
        }
    }

    @Test
    public void testAssociationSelect() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            ResultAutoMapper mapper = sqlSession.getMapper(ResultAutoMapper.class);
            Employee employee = mapper.getEmpAndDeptByAssociation(1);
            System.out.println("employee = " + employee);
        }
    }
}
