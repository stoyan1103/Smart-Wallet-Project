package services.imp;

import core.UserSessionManager;
import core.commands.Executable;
import core.commands.WalletTypeCommandInterpreter;
import core.commands.imp.WalletTypeCommandInterpreterImpl;
import entities.user.User;
import entities.wallet.StandardWallet;
import entities.wallet.Wallet;
import entities.wallet.WalletStatus;
import repositories.WalletRepository;
import services.WalletService;

import java.lang.reflect.InvocationTargetException;
import java.util.Currency;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static common.LogMessages.*;
import static common.SystemErrors.*;

public class WalletServiceImpl implements WalletService {

    private final UserSessionManager sessionManager;
    private final WalletRepository walletRepository;

    public WalletServiceImpl(UserSessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.walletRepository = new WalletRepository();
    }

    @Override
    public String createNewWallet(Currency currency, String walletType) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        validateActiveSession();

        WalletTypeCommandInterpreter walletTypeCommandInterpreter = new WalletTypeCommandInterpreterImpl(sessionManager, walletRepository);
        Executable executable = walletTypeCommandInterpreter.interpretCommand(currency, walletType);
        String result = executable.execute();

        return result;
    }

    @Override
    public String deposit(UUID walletId, double amount) {

        Wallet wallet = getCurrentlyActiveUserWallet(walletId);

        wallet.deposit(amount);

        return SUCCESSFULLY_DEPOSITED_AMOUNT.formatted(wallet.getBalance(), wallet.getCurrency());
    }

    @Override
    public String transfer(UUID walletId, String receiverUsername, double amount) {

        Wallet senderWallet = getCurrentlyActiveUserWallet(walletId);
        Wallet receiverWallet = walletRepository.getAll().stream()
                .filter(w -> w.getOwnerUsername().equals(receiverUsername) && w instanceof StandardWallet)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(NO_WALLET_FOUND_FOR_RECEIVER.formatted(receiverUsername)));

        boolean isSenderWalletActive = senderWallet.getStatus() == WalletStatus.ACTIVE;
        boolean isReceiverWalletActive = receiverWallet.getStatus() == WalletStatus.ACTIVE;
        boolean isSameCurrency = senderWallet.getCurrency().equals(receiverWallet.getCurrency());

        if (!(isSenderWalletActive && isReceiverWalletActive && isSameCurrency)) {
            throw new IllegalStateException(TRANSFER_CRITERIA_NOT_MET);
        }

        senderWallet.withdraw(amount);
        receiverWallet.deposit(amount);

        return SUCCESSFUL_FUNDS_TRANSFER.formatted(
                senderWallet.getOwnerUsername(),
                amount,
                receiverUsername,
                senderWallet.getBalance());
    }

    @Override
    public String changeWalletStatus(UUID walletId, String newStatus) {

        Wallet wallet = getCurrentlyActiveUserWallet(walletId);

        WalletStatus status;
        switch (newStatus) {
            case "ACTIVE" -> status = WalletStatus.ACTIVE;
            case "INACTIVE" -> status = WalletStatus.INACTIVE;
            default -> throw new IllegalArgumentException(INCORRECT_WALLET_STATUS);
        }

        wallet.setStatus(status);

        return SUCCESSFULLY_CHANGED_WALLET_STATUS.formatted(status);
    }

    @Override
    public String getMyWallets() {

        validateActiveSession();

        User activeUser = sessionManager.getActiveSession();

        List<Wallet> activeUserWallets = walletRepository.getAll().stream()
                .filter(w -> w.getOwnerId().equals(activeUser.getId()))
                .toList();

        if (activeUserWallets.isEmpty()) {
            return ZERO_WALLETS;
        }

        return activeUserWallets.stream().map(Wallet::toString).collect(Collectors.joining(System.lineSeparator()));
    }

    private Wallet getCurrentlyActiveUserWallet(UUID walletId) {
        validateActiveSession();

        User activeUser = sessionManager.getActiveSession();

        Wallet wallet = walletRepository.getAll().stream()
                .filter(w -> w.getOwnerId().equals(activeUser.getId()) && w.getId().equals(walletId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(WALLET_NOT_ASSOCIATED_WITH_THIS_USER.formatted(activeUser.getUsername())));

        return wallet;
    }

    private void validateActiveSession() {
        if (!sessionManager.hasActiveSession()) {
            throw new IllegalStateException(NO_ACTIVE_USER_SESSION_FOUND);
        }
    }
}
