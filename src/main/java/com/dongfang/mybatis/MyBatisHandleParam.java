package com.dongfang.mybatis;

import com.dongfang.mybatis.bean.Employee;
import com.dongfang.mybatis.dao.SelectParamMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/*
 * 单个参数：mybatis不会做特殊处理
 *         #{参数名} 取出参数值
 *
 * */
public class MyBatisHandleParam {
    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "MyBatis/mybatis-conf.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

    @Test
    public void testSingleParam() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            SelectParamMapper mapper = sqlSession.getMapper(SelectParamMapper.class);
            Employee employee = mapper.getEmpById(6);
            System.out.println("employee = " + employee);
        }

    }
}
