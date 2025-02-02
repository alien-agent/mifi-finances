package org.dvpashkevich.mifiwallet.cli;

import org.dvpashkevich.mifiwallet.models.*;
import org.dvpashkevich.mifiwallet.services.UserService;
import org.dvpashkevich.mifiwallet.services.WalletService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommandLineInterface {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";

    private Scanner scanner = new Scanner(System.in);
    private UserService userService;
    private WalletService walletService;
    private boolean isRunning = true;

    public CommandLineInterface(UserService userService) {
        this.userService = userService;
    }

    public void printCommand(String command) {
        System.out.println(ANSI_BLUE + command + ANSI_RESET);
    }

    public void printDescription(String description) {
        System.out.println(ANSI_YELLOW + description + ANSI_RESET);
    }

    public void start() {
        System.out.println();
        System.out.println(ANSI_BLUE + "Добро пожаловать в калькулятор расходов!" + ANSI_RESET);
        System.out.println(ANSI_PURPLE_BACKGROUND + ANSI_BLACK + "Пожалуйста используйте команду exit для выхода во избежание потери данных." + ANSI_RESET);
        System.out.println();

        showAuthMenu();

        while (isRunning) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            String[] parts = input.split(" ");
            String command = parts[0].toLowerCase();

            if (userService.getCurrentUser() == null) {
                handleUnauthCommand(command, parts);
            } else {
                handleAuthCommand(command, parts);
            }
        }
    }

    private void handleUnauthCommand(String command, String[] parts) {
        switch (command) {
            case "login":
                handleLogin(parts);
                break;
            case "register":
                handleRegister(parts);
                break;
            case "exit":
                isRunning = false;
                break;
            default:
                System.out.println("Доступные команды: login, register, exit");
        }
    }

    private void handleAuthCommand(String command, String[] parts) {
        switch (command) {
            case "add-income":
                handleAddTransaction(parts, Transaction.Type.INCOME);
                break;
            case "add-expense":
                handleAddTransaction(parts, Transaction.Type.EXPENSE);
                break;
            case "create-category":
                handleCreateCategory(parts);
                break;
            case "set-budget":
                handleSetBudget(parts);
                break;
            case "view-summary":
                handleViewSummary(parts);
                break;
            case "view-budgets":
                handleViewBudgets();
                break;
            case "view-categories":
                handleViewCategories(parts);
                break;
            case "logout":
                userService.logout();
                walletService = null;
                System.out.println("Вы вышли из системы");
                break;
            case "exit":
                userService.exit();
                isRunning = false;
                break;
            default:
                System.out.println("Неизвестная команда");
        }
    }

    private void handleLogin(String[] parts) {
        if (parts.length != 3) {
            System.out.println("Формат: login <логин> <пароль>");
            return;
        }
        String login = parts[1];
        String password = parts[2];
        if (userService.login(login, password)) {
            walletService = new WalletService(userService.getCurrentUser().getWallet());
            System.out.println("Успешный вход!");
            System.out.println();
            showMainMenu();
        } else {
            System.out.println("Ошибка входа");
        }

    }

    private void handleRegister(String[] parts) {
        if (parts.length != 3) {
            System.out.println("Формат: register <логин> <пароль>");
            return;
        }
        String login = parts[1];
        String password = parts[2];
        if (userService.register(login, password)) {
            System.out.println("Регистрация успешна");
        } else {
            System.out.println("Логин занят");
        }
    }

    private void handleViewCategories(String[] parts) {
        if (parts.length < 2) {
            System.out.println("Формат: view-categories <категория1> <категория2> ...");
            return;
        }

        List<Category> validCategories = new ArrayList<>();
        List<String> invalidCategories = new ArrayList<>();

        for (int i = 1; i < parts.length; i++) {
            Category category = findCategory(parts[i]);
            if (category == null) {
                invalidCategories.add(parts[i]);
            } else {
                validCategories.add(category);
            }
        }

        invalidCategories.forEach(c -> System.out.println("Категория не найдена: " + c));

        if (!validCategories.isEmpty()) {
            double totalIncome = walletService.getIncomeByCategories(validCategories);
            double totalExpense = walletService.getExpenseByCategories(validCategories);
            System.out.printf("Сумма доходов: %.2f\n", totalIncome);
            System.out.printf("Сумма расходов: %.2f\n", totalExpense);
        }
    }

    private void showAuthMenu() {
        System.out.println("Доступные команды:");
        printCommand("login [логин] [пароль]");
        printDescription("--- Авторизоваться");
        printCommand("register [логин] [пароль]");
        printDescription("--- Зарегистрировать пользователя");
        printCommand("exit");
        printDescription("--- Завершить работу приложения");
    }

    private void showMainMenu() {
        System.out.println("Доступные команды:");
        printCommand("add-income [сумма] [категория]");
        printDescription("--- Добавить поступление средств");
        printCommand("add-expense [сумма] [категория]");
        printDescription("--- Добавить трату средств");
        printCommand("create-category [название]");
        printDescription("--- Создать категорию");
        printCommand("set-budget [категория] [лимит]");
        printDescription("--- Установить бюджет на категорию");
        printCommand("view-categories [категория_1] ...");
        printDescription("--- Получить сводку по выбранным категориям");
        printCommand("view-summary");
        printDescription("--- Получить общую сводку по балансу");
        printCommand("view-budgets");
        printDescription("--- Получить сводку бюджетов");
        printCommand("logout");
        printDescription("--- Выйти из аккаунта");
        printCommand("exit");
        printDescription("--- Завершить работу приложения");
    }

    private void handleAddTransaction(String[] parts, Transaction.Type type) {
        if (parts.length != 3) {
            System.out.println("Неверный формат");
            return;
        }
        try {
            double amount = Double.parseDouble(parts[1]);
            if (amount <= 0) {
                System.out.println("Сумма должна быть положительной");
                return;
            }
            String categoryName = parts[2];
            Category category = findCategory(categoryName);
            if (category == null) {
                System.out.println("Категория не найдена");
                return;
            }
            if (type == Transaction.Type.INCOME) {
                walletService.addIncome(amount, category);
            } else {
                walletService.addExpense(amount, category);
            }
            System.out.println("Операция добавлена");
            checkFinancialHealth();
        } catch (NumberFormatException e) {
            System.out.println("Неверная сумма");
        }
    }

    private Category findCategory(String name) {
        return userService.getCurrentUser().getWallet().getCategories().stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    private void checkFinancialHealth() {
        double income = walletService.getTotalIncome();
        double expense = walletService.getTotalExpense();
        if (expense > income) {
            System.out.println("Внимание: расходы превышают доходы!");
        }
    }

    private void handleCreateCategory(String[] parts) {
        if (parts.length != 2) {
            System.out.println("Неверный формат");
            return;
        }
        String name = parts[1];
        Category category = walletService.createCategory(name);
        if (category != null) {
            System.out.println("Категория создана");
        } else {
            System.out.println("Категория уже существует");
        }
    }

    private void handleSetBudget(String[] parts) {
        if (parts.length != 3) {
            System.out.println("Неверный формат");
            return;
        }
        String categoryName = parts[1];
        Category category = findCategory(categoryName);
        if (category == null) {
            System.out.println("Категория не найдена");
            return;
        }
        try {
            double limit = Double.parseDouble(parts[2]);
            if (limit <= 0) {
                System.out.println("Лимит должен быть положительным");
                return;
            }
            walletService.setBudget(category, limit);
            System.out.println("Бюджет установлен");
        } catch (NumberFormatException e) {
            System.out.println("Неверный лимит");
        }
    }

    private void handleViewSummary(String[] parts) {
        double income = walletService.getTotalIncome();
        double expense = walletService.getTotalExpense();
        System.out.printf("Общие доходы: %.2f\n", income);
        System.out.printf("Общие расходы: %.2f\n", expense);
        System.out.printf("Баланс: %.2f\n", (income - expense));
    }

    private void handleViewBudgets() {
        userService.getCurrentUser().getWallet().getBudgets().forEach(b -> {
            System.out.printf("Категория: %s, Лимит: %.2f, Потрачено: %.2f, Остаток: %.2f\n",
                    b.getCategory().getName(), b.getLimit(), b.getCurrentSpent(), b.getRemaining());
        });
    }
}