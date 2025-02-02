package org.dvpashkevich.mifiwallet.models;

import java.util.ArrayList;
import java.util.List;

public class Wallet implements java.io.Serializable {
    private List<Transaction> transactions;
    private List<Category> categories;
    private List<Budget> budgets;

    public Wallet() {
        transactions = new ArrayList<>();
        categories = new ArrayList<>();
        budgets = new ArrayList<>();
    }

    // Геттеры
    public List<Transaction> getTransactions() { return transactions; }
    public List<Category> getCategories() { return categories; }
    public List<Budget> getBudgets() { return budgets; }
}