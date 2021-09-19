package com.example.salaries.Models;

public class Transactions {
    public String date;
    public String amount;
    public String name;
    public int currentCredit;

    public Transactions() { }

    public Transactions(String date, String amount, String name, int currentCredit) {
        this.date = date;
        this.amount = amount;
        this.name = name;
        this.currentCredit = currentCredit;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public int getCurrentCredit() {
        return currentCredit;
    }
}