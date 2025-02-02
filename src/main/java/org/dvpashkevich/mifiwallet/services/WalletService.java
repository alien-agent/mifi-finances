package org.dvpashkevich.mifiwallet.services;

import org.dvpashkevich.mifiwallet.models.*;

import java.util.List;
import java.util.Optional;

public class WalletService {
    private Wallet wallet;

    public WalletService(Wallet wallet) {
        this.wallet = wallet;
    }

    public void addIncome(double amount, Category category) {
        wallet.getTransactions().add(new Transaction(Transaction.Type.INCOME, amount, category, java.time.LocalDate.now()));
    }

    public void addExpense(double amount, Category category) {
        Transaction expense = new Transaction(Transaction.Type.EXPENSE, amount, category, java.time.LocalDate.now());
        wallet.getTransactions().add(expense);
        updateBudgets(category, amount);
    }

    private void updateBudgets(Category category, double amount) {
        Optional<Budget> budgetOpt = wallet.getBudgets().stream()
                .filter(b -> b.getCategory().equals(category))
                .findFirst();
        if (budgetOpt.isPresent()) {
            Budget budget = budgetOpt.get();
            budget.addExpense(amount);
            if (budget.getCurrentSpent() > budget.getLimit()) {
                System.out.println("Превышен лимит бюджета для категории: " + category.getName());
            }
        }
    }

    public Category createCategory(String name) {
        Category category = new Category(name);
        if (wallet.getCategories().contains(category)) {
            return null;
        }
        wallet.getCategories().add(category);
        return category;
    }

    public void setBudget(Category category, double limit) {
        wallet.getBudgets().removeIf(b -> b.getCategory().equals(category));
        wallet.getBudgets().add(new Budget(category, limit));
    }

    public double getTotalIncome() {
        return wallet.getTransactions().stream()
                .filter(t -> t.getType() == Transaction.Type.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalExpense() {
        return wallet.getTransactions().stream()
                .filter(t -> t.getType() == Transaction.Type.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getIncomeByCategories(List<Category> categories) {
        return wallet.getTransactions().stream()
                .filter(t -> t.getType() == Transaction.Type.INCOME)
                .filter(t -> categories.contains(t.getCategory()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getExpenseByCategories(List<Category> categories) {
        return wallet.getTransactions().stream()
                .filter(t -> t.getType() == Transaction.Type.EXPENSE)
                .filter(t -> categories.contains(t.getCategory()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
}