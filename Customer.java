package com.example.banking_system_fx;
public class Customer  {
    private String name, cnp;
    private String role;
    private int age;
    private String sex;
    private String username;
    private final String password;
    public Customer(String name, String cnp, int age, String sex, String username, String password, String role) {
        this.name = name;
        this.cnp = cnp;
        this.age = age;
        this.sex = sex;
        this.username = username;
        this.password = password;
        this.role=role;
    }
    public Customer(String name, String cnp, int age, String sex, String username, String password) {
        this.name = name;
        this.cnp = cnp;
        this.age = age;
        this.sex = sex;
        this.username = username;
        this.password = password;
    }

    public Customer(String username, String password, String role) {
        this.username=username;
        this.password=password;
        this.role=role;
    }

    public String getRole() {
        return role;
    }

    public Customer(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public Customer(String name, String cnp, int age, String sex) {
        this.name = name;
        this.cnp = cnp;
        this.age = age;
        this.sex = sex;
        this.username = "";
        this.password = "";
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public String getCNP() {
        return cnp;
    }

    public int getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }

    public String getCustomerName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name + ", " + this.cnp + ", " + this.age + ", " + this.sex;
    }

    public Thread getAccount() {
        return null;
    }
}
