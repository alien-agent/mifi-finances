package org.dvpashkevich.mifiwallet.services;

import org.dvpashkevich.mifiwallet.models.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileDataService {
    private static final String DATA_DIR = "data/";

    public void saveUsers(List<User> users) {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) dir.mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_DIR + "__users.dat"))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.out.println("Ошибка сохранения данных: " + e);
        }
    }

    public List<User> loadUsers() {
        File file = new File(DATA_DIR + "__users.dat");
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка загрузки данных: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}