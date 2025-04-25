package core.commands;

import core.UserSessionManager;
import repositories.WalletRepository;

import java.util.Currency;

public abstract class WalletTypeCommand implements Executable {

    private final UserSessionManager sessionManager;
    private final WalletRepository walletRepository;
    private final Currency currency;

    protected WalletTypeCommand(UserSessionManager sessionManager, WalletRepository walletRepository, Currency currency) {
        this.sessionManager = sessionManager;
        this.walletRepository = walletRepository;
        this.currency = currency;
    }

    public UserSessionManager getSessionManager() {
        return sessionManager;
    }

    public WalletRepository getWalletRepository() {
        return walletRepository;
    }

    public Currency getCurrency() {
        return currency;
    }
}