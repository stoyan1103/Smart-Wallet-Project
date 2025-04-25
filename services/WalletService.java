package services;

import java.lang.reflect.InvocationTargetException;
import java.util.Currency;
import java.util.UUID;

public interface WalletService {

    String createNewWallet(Currency currency, String walletType) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

    String getMyWallets();

    String deposit(UUID walletId, double amount);

    String transfer(UUID walletId, String receiverUsername, double amount);

    String changeWalletStatus(UUID walletId, String newStatus);
}
