package com.example.salaries.Models;

public class EmployeeModel {
    String employId;
    String name;
    String D_O_B;
    String mobile;
    String designation;
    String salary;
    int balance;
    int credit;

    public EmployeeModel() { //Primary Constructor is Necessary for Firebase Recycler.
    }

    public EmployeeModel(String employeeId, String name, String D_O_B, String mobile, String designation, String salary, int balance, int credit) {
        this.employId = employeeId;
        this.name = name;
        this.D_O_B = D_O_B;
        this.mobile = mobile;
        this.designation = designation;
        this.salary = salary;
        this.balance = balance;
        this.credit = credit;
    }

    //Setters for Model
    public void setEmployId(String employId) {
        this.employId = employId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setD_O_B(String d_O_B) {
        this.D_O_B = d_O_B;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    //Getters for Model
    public String getEmployId() {
        return employId;
    }

    public String getName() {
        return name;
    }

    public String getD_O_B() {
        return D_O_B;
    }

    public String getMobile() {
        return mobile;
    }

    public String getDesignation() {
        return designation;
    }

    public String getSalary() {
        return salary;
    }

    public int getBalance() {
        return balance;
    }

    public int getCredit() {
        return credit;
    }
}