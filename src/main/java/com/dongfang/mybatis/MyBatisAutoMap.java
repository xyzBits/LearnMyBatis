package com.dongfang.mybatis;

import com.dongfang.mybatis.bean.Department;
import com.dongfang.mybatis.bean.Employee;
import com.dongfang.mybatis.dao.DepartmentMapper;
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

    @Test
    public void testSelectByStep() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            ResultAutoMapper mapper = sqlSession.getMapper(ResultAutoMapper.class);
            Employee employee = mapper.getEmpStepByStep(1);
            System.out.println("employee = " + employee);
        }
    }

    @Test
    public void testLazyLoading() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            ResultAutoMapper mapper = sqlSession.getMapper(ResultAutoMapper.class);
            Employee employee = mapper.getEmpStepByStep(1);
            // 开启了懒加载
            // Preparing: select * from t_employee where id = ? 查询的时候，只发送一条sql，不查询部门信息
            System.out.println("employee.getLastName() = " + employee.getLastName());
//            System.out.println("employee.getDepartment() = " + employee.getDepartment());
            /**
             * 直到我们需要部门信息时，才调用sql去查询
            * DEBUG 01-04 10:11:17,748 ==>  Preparing: select * from t_employee where id = ?   (BaseJdbcLogger.java:145)
             * DEBUG 01-04 10:11:17,794 ==> Parameters: 1(Integer)  (BaseJdbcLogger.java:145)
             * DEBUG 01-04 10:11:17,848 <==      Total: 1  (BaseJdbcLogger.java:145)
             * employee.getLastName() = Dong fang
             * DEBUG 01-04 10:11:17,849 ==>  Preparing: select * from t_department where id = ?   (BaseJdbcLogger.java:145)
             * DEBUG 01-04 10:11:17,853 ==> Parameters: 1(Integer)  (BaseJdbcLogger.java:145)
             * DEBUG 01-04 10:11:17,856 <==      Total: 1  (BaseJdbcLogger.java:145)
             * employee.getDepartment() = {"id":1,"departmentName":"development-department"}
            * */
        }
    }

    @Test
    public void testCollectionProperty() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            DepartmentMapper mapper = sqlSession.getMapper(DepartmentMapper.class);
            Department deptWithEmp = mapper.getDeptWithEmpById(2);
            System.out.println("deptWithEmp = " + deptWithEmp);
        }
    }

    @Test
    public void testSelectCollectionOverStep() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            DepartmentMapper mapper = sqlSession.getMapper(DepartmentMapper.class);
            Department deptWithEmp = mapper.getDeptWithEmpStepById(1);
            System.out.println("deptWithEmp.getDepartmentName() = " + deptWithEmp.getDepartmentName());
            System.out.println("deptWithEmp = " + deptWithEmp);
        }
    }
}
