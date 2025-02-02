package org.dvpashkevich.mifiwallet.services;

import org.dvpashkevich.mifiwallet.models.User;
import org.dvpashkevich.mifiwallet.models.Wallet;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private List<User> users;
    private User currentUser;
    private FileDataService fileDataService;

    public UserService(FileDataService fileDataService) {
        this.fileDataService = fileDataService;
        this.users = this.fileDataService.loadUsers();
    }

    public boolean register(String login, String password) {
        if (users.stream().anyMatch(u -> u.getLogin().equals(login)) || login.startsWith("__")) {
            return false;
        }
        users.add(new User(login, password));
        return true;
    }

    public boolean login(String login, String password) {
        User user = users.stream()
                .filter(u -> u.getLogin().equals(login) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
        if (user != null) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public User getCurrentUser() { return currentUser; }
    public void logout() {
        currentUser = null;
    }

    public void exit() {
        this.fileDataService.saveUsers(this.users);
    }
}