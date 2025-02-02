package org.dvpashkevich.mifiwallet.models;

public class Budget implements java.io.Serializable {
    private Category category;
    private double limit;
    private double currentSpent;

    public Budget(Category category, double limit) {
        this.category = category;
        this.limit = limit;
        this.currentSpent = 0;
    }

    // Геттеры и методы
    public Category getCategory() { return category; }
    public double getLimit() { return limit; }
    public double getCurrentSpent() { return currentSpent; }
    public void addExpense(double amount) {
        currentSpent += amount;
    }
    public double getRemaining() { return limit - currentSpent; }
}