package core.commands;

import core.UserSessionManager;
import entities.user.User;
import entities.wallet.StandardWallet;
import entities.wallet.Wallet;
import repositories.WalletRepository;

import java.util.Currency;

import static common.SystemErrors.STANDARD_WALLET_COUNT_LIMIT_REACHED;

public class StandardWalletCommand extends WalletTypeCommand {

    protected StandardWalletCommand(UserSessionManager userSessionManager, WalletRepository walletRepository, Currency currency) {
        super(userSessionManager, walletRepository, currency);
    }

    @Override
    public String execute() {
        User activeUser = super.getSessionManager().getActiveSession();

        Wallet wallet = new StandardWallet(activeUser.getId(), activeUser.getUsername(), super.getCurrency());

        boolean alreadyExistingStandardWallet = super.getWalletRepository().getAll().stream()
                .anyMatch(w -> w.getOwnerId().equals(activeUser.getId()) && w instanceof StandardWallet);

        if (alreadyExistingStandardWallet) {
            throw new IllegalStateException(STANDARD_WALLET_COUNT_LIMIT_REACHED);
        }

        super.getWalletRepository().save(wallet.getId(), wallet);

        return wallet.toString();
    }
}