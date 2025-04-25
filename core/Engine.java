package core;

import core.commands.Command;
import services.WalletService;
import services.UserService;
import services.imp.UserServiceImpl;
import services.imp.WalletServiceImpl;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Currency;
import java.util.Scanner;
import java.util.UUID;

public class Engine implements Runnable {

    private final Scanner scanner;
    private final UserSessionManager sessionManager;
    private final UserService userService;
    private final WalletService walletService;

    public Engine() {
        this.scanner = new Scanner(System.in);
        this.sessionManager = new UserSessionManager();
        this.userService = new UserServiceImpl(sessionManager);
        this.walletService = new WalletServiceImpl(sessionManager);
    }

    @Override
    public void run() {

        System.out.println("Welcome to Smart Wallet!");

        while (true) {
            String result;
            try {
                result = processInput();
                if (result.equals("Exit")) {
                    break;
                }
                System.out.println("\033[0;34m" + result + "\033[0m");
            } catch (RuntimeException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException |
                     InstantiationException | IllegalAccessException e) {
                result = e.getMessage();
                System.err.println(result);
            }
        }
    }

    private String processInput() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        String result;

        String[] tokens = scanner.nextLine().split("\\s+");

        Command command = Command.valueOf(tokens[0]);
        String[] data = Arrays.stream(tokens).skip(1).toArray(String[]::new);


        result = switch (command) {
            case Login -> userService.login(data[0], data[1]);
            case Register -> userService.register(data[0], data[1]);
            case Logout -> userService.logout();
            case NewWallet -> walletService.createNewWallet(Currency.getInstance(data[0]), data[1]);
            case MyWallets -> walletService.getMyWallets();
            case ChangeWalletStatus -> walletService.changeWalletStatus(UUID.fromString(data[0]), data[1]);
            case Deposit -> walletService.deposit(UUID.fromString(data[0]), Double.parseDouble(data[1]));
            case Transfer -> walletService.transfer(UUID.fromString(data[0]), data[1], Double.parseDouble(data[2]));
            case Exit -> Command.Exit.name();
        };

        return result;
    }
}
