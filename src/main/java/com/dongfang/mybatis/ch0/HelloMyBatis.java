package com.dongfang.mybatis.ch0;

import com.dongfang.mybatis.ch0.bean.Employee;
import com.dongfang.mybatis.ch0.dao.EmployeeMapper;
import com.dongfang.mybatis.ch0.dao.EmployeeMapperAnnotation;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class HelloMyBatis {
    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "MyBatis/mybatis-conf.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

    /**
     * 根据xml文件创建SqlSessionFactory，这是全局配置文件
     *      有数据源运行环境的信息
     *
     *
     *
     *      1、根据xml配置文件（全局配置文件），创建一个SqlSessionFactory对象
     *              数据源的一些运行信息
     *      2、sql映射文件，配置了每一个sql，以及sql的封闭规则等
     *      3、将sql映射文件注册在全局配置文件中
     *      4、写代码：
     *              1）、根据全局配置文件得到SqlSessionFactory
     *              2）、使用sqlSessionFactory得到SqlSession对象，使用它来进行CRUD
     *                  一个SqlSession就是代表和数据库的一次会话，用完关闭
     *              3）、使用SQL的唯一标识来告诉MaBatis执行哪个sql，sql保存在sql映射文件
     * @throws IOException
     */
    @Test
    public void sqlSessionFactory() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

        // 获取SqlSession实例，能直接执行已经映射的SQL语句
        SqlSession sqlSession = sqlSessionFactory.openSession();
        System.out.println("sqlSession = " + sqlSession);

        // arg1 sql的唯一标识


        Employee employee = sqlSession.selectOne("com.dongfang.mybatis.ch0.dao.EmployeeDao.getEmpById", 1);
        System.out.println("employee = " + employee);
    }

    @Test
    public void testMapperInterfaceProxy() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        // 获取接口的实现对象，然后调用接口的方法
        EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
        // MyBatis会为接口自动地创建一个代理对象，代理对象去执行CRUD
        System.out.println("employeeMapper.getClass() = " + employeeMapper.getClass());
        System.out.println("employeeMapper.getEmpById(2) = " + employeeMapper.getEmpById(2));

    }

    @Test
    public void testAnnotationMapper() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        EmployeeMapperAnnotation employeeMapperAnnotation = sqlSession.getMapper(EmployeeMapperAnnotation.class);
        System.out.println("employeeMapperAnnotation.getEmpById(1) = " + employeeMapperAnnotation.getEmpById(1));
    }


}
