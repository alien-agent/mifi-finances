package org.dvpashkevich.mifiwallet.models;

import java.time.LocalDate;

public class Transaction implements java.io.Serializable {
    public enum Type { INCOME, EXPENSE }

    private Type type;
    private double amount;
    private Category category;
    private LocalDate date;

    public Transaction(Type type, double amount, Category category, LocalDate date) {
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    // Геттеры
    public Type getType() { return type; }
    public double getAmount() { return amount; }
    public Category getCategory() { return category; }
    public LocalDate getDate() { return date; }
}