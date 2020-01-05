package com.dongfang.mybatis.bean;

import org.apache.ibatis.type.Alias;

//@Alias("empp")
public class Employee {
    private Integer id;
    private String lastName;
    private String email;
    private String gender;

    private Integer deptId;

    public Employee() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"lastName\":\"")
                .append(lastName).append('\"');
        sb.append(",\"email\":\"")
                .append(email).append('\"');
        sb.append(",\"gender\":\"")
                .append(gender).append('\"');
        sb.append(",\"deptId\":")
                .append(deptId);
        sb.append('}');
        return sb.toString();
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public Employee(Integer id, String lastName, String email, String gender, Integer deptId) {
        this.id = id;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.deptId = deptId;
    }

    public Employee(Integer id, String lastName, String email, String gender) {
        this.id = id;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
