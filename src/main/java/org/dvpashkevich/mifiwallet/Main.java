package org.dvpashkevich.mifiwallet;

import org.dvpashkevich.mifiwallet.cli.CommandLineInterface;
import org.dvpashkevich.mifiwallet.services.FileDataService;
import org.dvpashkevich.mifiwallet.services.UserService;

public class Main {

    public static void main(String[] args) {
        FileDataService fileDataService = new FileDataService();
        UserService userService = new UserService(fileDataService);
        CommandLineInterface cli = new CommandLineInterface(userService);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Автосохранение данных...");
            userService.exit();
        }));

        cli.start();
    }
}