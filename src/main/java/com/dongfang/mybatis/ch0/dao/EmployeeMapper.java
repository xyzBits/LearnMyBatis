package com.dongfang.mybatis.ch0.dao;

import com.dongfang.mybatis.ch0.bean.Employee;

public interface EmployeeMapper {
    /**
     * 1、接口式编程
     *      原生     Dao ----> DaoImpl
     *      mybatis Mapper ----> xxxMapper.xml
     * 2、SqlSession代表和数据库的一次会话，用完必须关闭
     * 3、SqlSession和Connection一样都是非线程安全的，每次使用都应该获取新的对象，不要成为类的成员变量
     * 4、mapper接口没有实现类，但是mybatis会为这个接口生成一个代理对象
     *      sqlSession.getMapper(EmployeeMapper.class)拿到的是代理对象，
     *      代理对象的产生是将接口与xml进行绑定了
     * 5、两个重要的配置文件：
     *      MyBatis全局配置文件，包含数据库连接池信息，事务管理器信息等系统运行环境
     *      SQL映射文件，保存了每一个sql语句的映射信息，将sql抽取出来
     * @param id
     * @return
     */
    Employee getEmpById(Integer id);

    void addEmp(Employee emp);

    void updateEmp(Employee emp);

    void deleteEmpById(Integer id);
}
