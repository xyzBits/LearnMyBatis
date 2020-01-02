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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 总结： 参数多的时候会封装map，为了不混乱，我们可以使用@Param来指定封装时使用的key
 *         #{key}就可以取出map中的值
 *
 *    参数中值的获取
 *          1、#{}可以获取map中的值或者pojo对象属性的值
 *          2、${}可以获取map中的值或者pojo对象属性的值
 *          区别：#{}是以预编译的形式，将参数设置到sql语句中 PreparedStatement
 *               ${}取出的值直接拼装在sql语句中，会有安全问题，无法防止sql注入
 *               大多情况下，我们取参数的值，都应该使用#{}
 *               ${}比如分表，按照年份拆分 select * from
 *               原生jdbc不支持点位符的地方，我们就可以使用${}进行取值
 *      where id = ${id} and last_name = #{lastName}
 *     Preparing: select id, last_name lastName, gender, email from t_employee where id = 6 and last_name = ?
 */

/*
 * 单个参数：mybatis不会做特殊处理
 *         #{参数名} 取出参数值  一个参数可以任意写 id = #{idddd}
 * 多个参数：mybatis会做特殊处理
 *          多个参数会被封装成一个map
 *          key：param1...paramN，或者参数的索引也可以
 *          value: 传入的参数值
 *          #{}就是从map中获取指定的key的值
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

    /**
     * 方法 Employee getEmpByIdAndLastName(Integer id, String lastName)
     * sql          select id, last_name lastName, gender, email from t_employee
     *              where id = #{id} and last_name = #{lastName}
     * 出现异常：Parameter 'id' not found. Available parameters are [0, 1, param1, param2]
     * 可以写成 where id = #{param1} and last_name = #{param2} 就可以
     *
     * 命名参数：明确指定封装参数map的key:@Param("id")
     *      多个参数会被封装成一个map:
     *          key，使用@Param注解指定的值
     *          value 参数值
     *          #{指定的key} 取出对应的参数值
     *          getEmpByIdAndLastName(@Param("id") Integer id, @Param("lastName") String lastName);
     *          where id = #{id} and last_name = #{lastName}
     *
     * */
    @Test
    public void testTwoParam() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            SelectParamMapper mapper = sqlSession.getMapper(SelectParamMapper.class);
            Employee employee = mapper.getEmpByIdAndLastName(6, "Jerry");
            System.out.println("employee = " + employee);
        }
    }

    /**
     * 如果多个参数正好是我们业务逻辑的数据模型，我们就可以直接传入pojo
     * getEmpByIdAntOtherEmp(@Param("id") Integer id, @Param("employee") Employee employee);
     * where id = #{id} and last_name = #{employee.lastName}
    * */
    @Test
    public void testPojoParam() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            SelectParamMapper mapper = sqlSession.getMapper(SelectParamMapper.class);
            Employee emp = new Employee(null, "Jerry", "Jerry@qq.com", "M");
            Employee employee = mapper.getEmpByIdAntOtherEmp(6, emp);
            System.out.println("employee = " + employee);
        }
    }

    /**
     *
     * 如果多个参数不是业务模型中的数据，没有对应的pojo，不经常使用，为了方便，我们也可以传入map
     *      #{key}取出对应的值
     */
    @Test
    public void testParamMap() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            SelectParamMapper mapper = sqlSession.getMapper(SelectParamMapper.class);
            Map<String, Object> map = new HashMap<>();
            map.put("id", 6);
            map.put("lastName", "Jerry");
            Employee employee = mapper.getEmpByMap(map);
            System.out.println("employee = " + employee);
        }
    }


    /**
     * 如果多个参数不是业务模型中的数据，但是要经常使用，推荐来编写一个TO Transfer Object 数据传输对象
    * */

    /**
     * 特别注意：如果是Collection(List Set)类型或者是数组，也会特殊处理，也是把传入的list
     * 或者数据，封装在map中
     *          key: collection
     *          List list
     *          数组 array
     *      取值：取出第一个id的值 #{list[0]}
    * */
    @Test
    public void testCollectionParam() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            SelectParamMapper mapper = sqlSession.getMapper(SelectParamMapper.class);
            List<Integer> ids = Collections.singletonList(1);
            Employee employee = mapper.getEmpByIds(ids);
            System.out.println("employee = " + employee);
        }
    }

    /**
     *     Employee getEmpByTableAndId(@Param("tableName") String tableName, @Param("id") Integer id);
     *         select id, last_name lastName, gender, email
     *         from ${tableName}
     *         where id = #{id}
     *
     *    Preparing: select id, last_name lastName, gender, email from t_employee where id = ?
     *    动态地指定表名
     */
    @Test
    public void testDollarSign() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            SelectParamMapper mapper = sqlSession.getMapper(SelectParamMapper.class);
            Employee employee = mapper.getEmpByTableAndId("t_employee", 1);
            System.out.println("employee = " + employee);
        }
    }

    /**
     * #{}更丰富的用法
     *      规定参数的一些规则
     *      javaType jdbcType mode(存储过程) numericScale
     *      resultMap typeHandler jdcTypeName expression(未来准备支持的功能)
     *
     *      jdbcType通常需要在某种特定的条件下被设置
     *          在数据为null的时候，有些数据库可以不能识别mybatis对null的默认处理
     *          比如oracle，字段为null，报错
     *
     *          将字段设置为null，报错为 JdbcType OTHER无效的类型，因为mybatis对所有的null映射的都是
     *          原生jdbc中的other类型，oracle不能正确处理
     *          #{email, jdbcType=NULL}
     *
     *
     *          由于全局配置中，jdbcTypeForNull=OTHER， oracle不支持，两种解决方法都行
     *          1、#{email, jdbcType=NULL}
     *          2、JdbcTypeForNull=NULL
     *
     *          <setting name="jdbcTypeForNull" value="OTHER"/>
     */

    @Test
    public void testJdbcType() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            SelectParamMapper mapper = sqlSession.getMapper(SelectParamMapper.class);
            Employee employee = new Employee(null, "Jerry", null, "M");
            mapper.addEmp(employee);
            System.out.println("employee = " + employee);
        }
    }

}
