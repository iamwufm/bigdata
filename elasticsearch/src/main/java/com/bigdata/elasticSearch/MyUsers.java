package com.bigdata.elasticSearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Date:2023/10/26
 * Author:wfm
 * Desc:
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class MyUsers {

    private long empid;
    private int age;
    private float balance;
    private String name;
    private String gender;
    private String hobby;
    private String tag;

    public MyUsers(long empid, int age, float balance, String name, String gender, String hobby) {
        this.empid = empid;
        this.age = age;
        this.balance = balance;
        this.name = name;
        this.gender = gender;
        this.hobby = hobby;
        this.tag = hobby;
    }

    public MyUsers() {
    }

    @Override
    public String toString() {
        return "empid=" + empid +
                ", age=" + age +
                ", balance=" + balance +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", hobby='" + hobby + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }

    public long getEmpid() {
        return empid;
    }

    public void setEmpid(long empid) {
        this.empid = empid;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
