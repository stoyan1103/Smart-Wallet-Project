package core.commands;

import core.UserSessionManager;
import entities.user.User;
import entities.wallet.DisposableWallet;
import entities.wallet.Wallet;
import repositories.WalletRepository;

import java.util.Currency;

public class DisposableWalletCommand extends WalletTypeCommand {

    protected DisposableWalletCommand(UserSessionManager userSessionManager, WalletRepository walletRepository, Currency currency) {
        super(userSessionManager, walletRepository, currency);
    }

    @Override
    public String execute() {
        User activeUser = super.getSessionManager().getActiveSession();

        Wallet wallet = new DisposableWallet(activeUser.getId(), activeUser.getUsername(), super.getCurrency());
        super.getWalletRepository().save(wallet.getId(), wallet);

        return wallet.toString();
    }
}