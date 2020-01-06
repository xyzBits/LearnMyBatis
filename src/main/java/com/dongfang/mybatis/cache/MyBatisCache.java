package com.dongfang.mybatis.cache;

import com.dongfang.mybatis.bean.Employee;
import com.dongfang.mybatis.dao.EmployeeCrudMapper;
import com.dongfang.mybatis.dao.ResultAutoMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * 两级缓存：
 *      一级缓存：（本地缓存） sqlSession级别的缓存，一级缓存是一直开启的，新的session有新的缓存 ，不能共用
 *              sqlSession级别的一个Map
 *              与数据库同一次会话期间查询到的数据会存放在本地缓存中
 *              以后如果需要获取相同的数据，直接从缓存中拿，没必要再去查询数据库
 *
 *              一级缓存失败情况（没有使用到当前一级缓存的情况，效果是，还需要再向数据库发出查询）
 *                  1、sqlSession不同  第二次查询的数据，只会放在第二次会话的一级缓存中
 *                  2、sqlSession相同，但是查询条件不同（当前一级缓存中还没有这个数据）
 *                  3、sqlSession相同，但是两次查询之间执行了增删改操作（这次增删改可能对当前数据有影响）
 *                  4、sqlSession相同，手动清除了一级缓存 （缓存清空）
 *
 *      二级缓存： （全局缓存）；基于namespace级别的缓存，一个namespace对应一个二级缓存
 *              工作机制：
 *                  1、一个会话，查询一条数据，这个数据就会被放在当前会话的一级缓存中
 *                  2、如果会话关闭：一级缓存中的数据会被保存到二级缓存中；新的会话查询信息，就可以参照二级缓存中的内容
 *                  3、sqlSession====>EmployeeMapper ---> Employee
 *                                  >DepartmentMapper--->Department
 *                                  不同namespace查出的数据会放在自己对应的缓存中（map)
 *
 *                           效果：数据会从二级缓存中获取
 *                              查出的数据都会被默认放在一级缓存中，只有会话被提交或者关闭后，一级缓存中的数据才会转移到二级缓存中
 *                   使用：
 *                      1）、开户全局二级缓存配置: <setting name="cacheEnabled" value="true"/>
 *                      2）、去mapper.xml中配置使用二级缓存
 *                      3）、我们的pojo需要实现序列化接口
 *
 *                   和缓存有关的属性和设置：
 *                      1）、cacheEnabled=true false关闭缓存，关闭的是二级缓存 ，一级缓存一直开启
 *                      2）、每个select标签都有useCache=true
 *                                  useCache=false关闭，一级缓存仍然开启，二级缓存会被关闭
 *                      3）、每个增删改标签 flushCache=true  增删改后，一级二级都会清理
 *                                  增删改执行完后就会清理缓存
 *                                  测试flushCache=true一级缓存就清空了，二级缓存也会被清空
 *                                  查询 flushCache=false 如果我们改为true，每次查询之后会清理缓存 ，缓存是没有使用的
 *                      4）、sqlSession.clearCache() 只是清除当前session的一级缓存 ，不清理二级
 *                      5）、localCacheScope 本地缓存作用域，一级缓存 session，当前会话的所有数据保存在会话缓存中
 *                                          statement可以禁用一级缓存
 *
 *                                 使用缓存的顺序，先去二级，再去一级，再去数据库
 *
 */
public class MyBatisCache {
    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "MyBatis/mybatis-conf.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

    @Test
    public void testFirstLevelCache1() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            ResultAutoMapper mapper = sqlSession.getMapper(ResultAutoMapper.class);
            Employee empById = mapper.getEmpById(1);
            System.out.println("empById = " + empById);
            Employee empById1 = mapper.getEmpById(1);
            System.out.println("empById1 = " + empById1);
            System.out.println("empById.equals(empById1) = " + empById.equals(empById1));

            /**
             * 传入同一个id，只发送一次sql语句
            * DEBUG 01-05 23:21:52,202 ==>  Preparing: select * from t_employee where id = ?;   (BaseJdbcLogger.java:145)
             * DEBUG 01-05 23:21:52,267 ==> Parameters: 1(Integer)  (BaseJdbcLogger.java:145)
             * DEBUG 01-05 23:21:52,298 <==      Total: 1  (BaseJdbcLogger.java:145)
             * empById = {"id":1,"lastName":"Dong fang","email":"dongfang@gmail.com ","gender":"1","deptId":1,"dept":null}
             * empById1 = {"id":1,"lastName":"Dong fang","email":"dongfang@gmail.com ","gender":"1","deptId":1,"dept":null}
             * empById.equals(empById1) = true
            * */
        }
    }

    @Test
    public void testFirstLevelCache2() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            ResultAutoMapper mapper = sqlSession.getMapper(ResultAutoMapper.class);
            Employee empById = mapper.getEmpById(1);
            System.out.println("empById = " + empById);
            Employee empById1 = mapper.getEmpById(2);
            System.out.println("empById1 = " + empById1);
            System.out.println("empById.equals(empById1) = " + empById.equals(empById1));

            /**
             * 传入不同的id，就会发送两次sql语句
             * DEBUG 01-05 23:23:30,524 ==>  Preparing: select * from t_employee where id = ?;   (BaseJdbcLogger.java:145)
             * DEBUG 01-05 23:23:30,567 ==> Parameters: 1(Integer)  (BaseJdbcLogger.java:145)
             * DEBUG 01-05 23:23:30,593 <==      Total: 1  (BaseJdbcLogger.java:145)
             * empById = {"id":1,"lastName":"Dong fang","email":"dongfang@gmail.com ","gender":"1","deptId":1,"dept":null}
             * DEBUG 01-05 23:23:30,595 ==>  Preparing: select * from t_employee where id = ?;   (BaseJdbcLogger.java:145)
             * DEBUG 01-05 23:23:30,596 ==> Parameters: 2(Integer)  (BaseJdbcLogger.java:145)
             * DEBUG 01-05 23:23:30,597 <==      Total: 1  (BaseJdbcLogger.java:145)
             * empById1 = {"id":2,"lastName":"Dong fang","email":"Jerry@gmail.com","gender":"0","deptId":1,"dept":null}
             * empById.equals(empById1) = false
             * */
        }
    }

    @Test
    public void testFirstLevelCache3() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession();
             SqlSession sqlSession1 = sqlSessionFactory.openSession()) {
            ResultAutoMapper mapper = sqlSession.getMapper(ResultAutoMapper.class);
            Employee empById = mapper.getEmpById(1);
            System.out.println("empById = " + empById);

            // 使用第二个session进行查询
            ResultAutoMapper mapper1 = sqlSession1.getMapper(ResultAutoMapper.class);
            Employee empById1 = mapper1.getEmpById(1);
            System.out.println("empById1 = " + empById1);
            System.out.println("empById.equals(empById1) = " + empById.equals(empById1));

            /**
             * 第一次查询的数据，只会放在第一次会话的一级缓存中，
             * 第二次查询的数据，只会放在第二次会话的一级缓存中
             * DEBUG 01-05 23:27:58,994 ==>  Preparing: select * from t_employee where id = ?;   (BaseJdbcLogger.java:145)
             * DEBUG 01-05 23:27:59,041 ==> Parameters: 1(Integer)  (BaseJdbcLogger.java:145)
             * DEBUG 01-05 23:27:59,067 <==      Total: 1  (BaseJdbcLogger.java:145)
             * empById = {"id":1,"lastName":"Dong fang","email":"dongfang@gmail.com ","gender":"1","deptId":1,"dept":null}
             * DEBUG 01-05 23:27:59,072 ==>  Preparing: select * from t_employee where id = ?;   (BaseJdbcLogger.java:145)
             * DEBUG 01-05 23:27:59,073 ==> Parameters: 1(Integer)  (BaseJdbcLogger.java:145)
             * DEBUG 01-05 23:27:59,079 <==      Total: 1  (BaseJdbcLogger.java:145)
             * empById1 = {"id":1,"lastName":"Dong fang","email":"dongfang@gmail.com ","gender":"1","deptId":1,"dept":null}
             * empById.equals(empById1) = false
             */
        }
    }

    @Test
    public void testFirstLevelCache4() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            ResultAutoMapper mapper = sqlSession.getMapper(ResultAutoMapper.class);
            Employee empById = mapper.getEmpById(1);
            System.out.println("empById = " + empById);

            // 两次查询期间执行增删改
            EmployeeCrudMapper mapper1 = sqlSession.getMapper(EmployeeCrudMapper.class);
            mapper1.addEmp(new Employee("lastName", "email","1", 1));


            Employee empById1 = mapper.getEmpById(1);
            System.out.println("empById1 = " + empById1);
            System.out.println("empById.equals(empById1) = " + empById.equals(empById1));


            /**
             * DEBUG 01-05 23:37:09,906 ==>  Preparing: select * from t_employee where id = ?;   (BaseJdbcLogger.java:145)
             * DEBUG 01-05 23:37:09,963 ==> Parameters: 1(Integer)  (BaseJdbcLogger.java:145)
             * DEBUG 01-05 23:37:09,992 <==      Total: 1  (BaseJdbcLogger.java:145)
             * empById = {"id":1,"lastName":"Dong fang","email":"dongfang@gmail.com ","gender":"1","deptId":1,"dept":null}
             * DEBUG 01-05 23:37:09,996 ==>  Preparing: insert into t_employee (last_name, email, gender) values (?, ?, ?)   (BaseJdbcLogger.java:145)
             * DEBUG 01-05 23:37:09,997 ==> Parameters: lastName(String), email(String), 1(String)  (BaseJdbcLogger.java:145)
             * DEBUG 01-05 23:37:09,999 <==    Updates: 1  (BaseJdbcLogger.java:145)
             * DEBUG 01-05 23:37:10,000 ==>  Preparing: select * from t_employee where id = ?;   (BaseJdbcLogger.java:145)
             * DEBUG 01-05 23:37:10,001 ==> Parameters: 1(Integer)  (BaseJdbcLogger.java:145)
             * DEBUG 01-05 23:37:10,002 <==      Total: 1  (BaseJdbcLogger.java:145)
             * empById1 = {"id":1,"lastName":"Dong fang","email":"dongfang@gmail.com ","gender":"1","deptId":1,"dept":null}
             * empById.equals(empById1) = false
             */
        }
    }



    @Test
    public void testFirstLevelCache5() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            ResultAutoMapper mapper = sqlSession.getMapper(ResultAutoMapper.class);
            Employee empById = mapper.getEmpById(1);
            System.out.println("empById = " + empById);
            sqlSession.clearCache();
            // 缓存清除后，就会重新执行sql

            Employee empById1 = mapper.getEmpById(1);
            System.out.println("empById1 = " + empById1);
            System.out.println("empById.equals(empById1) = " + empById.equals(empById1));
            /**
             * DEBUG 01-05 23:39:45,948 ==>  Preparing: select * from t_employee where id = ?;   (BaseJdbcLogger.java:145)
             * DEBUG 01-05 23:39:46,009 ==> Parameters: 1(Integer)  (BaseJdbcLogger.java:145)
             * DEBUG 01-05 23:39:46,036 <==      Total: 1  (BaseJdbcLogger.java:145)
             * empById = {"id":1,"lastName":"Dong fang","email":"dongfang@gmail.com ","gender":"1","deptId":1,"dept":null}
             * DEBUG 01-05 23:39:46,038 ==>  Preparing: select * from t_employee where id = ?;   (BaseJdbcLogger.java:145)
             * DEBUG 01-05 23:39:46,039 ==> Parameters: 1(Integer)  (BaseJdbcLogger.java:145)
             * DEBUG 01-05 23:39:46,041 <==      Total: 1  (BaseJdbcLogger.java:145)
             * empById1 = {"id":1,"lastName":"Dong fang","email":"dongfang@gmail.com ","gender":"1","deptId":1,"dept":null}
             * empById.equals(empById1) = false
             */

        }
    }

    @Test
    public void testSecondLevelCache() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession();
             SqlSession sqlSession1 = sqlSessionFactory.openSession()) {
            ResultAutoMapper mapper = sqlSession.getMapper(ResultAutoMapper.class);
            Employee empById = mapper.getEmpById(1);
            System.out.println("empById = " + empById);

            sqlSession.close();
//            EmployeeCrudMapper mapper1 = sqlSession1.getMapper(EmployeeCrudMapper.class);
//            mapper1.addEmp(new Employee("lastName", "email","1", 1));
            // 第二次查询是从二级缓存中拿的数据，没有发送sql
            sqlSession.clearCache();
            ResultAutoMapper mapper2 = sqlSession1.getMapper(ResultAutoMapper.class);
            Employee empById1 = mapper2.getEmpById(1);
            System.out.println("empById1 = " + empById1);
        }
    }
}
