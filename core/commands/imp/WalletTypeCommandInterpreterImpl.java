package core.commands.imp;

import core.UserSessionManager;
import core.commands.Executable;
import core.commands.WalletTypeCommand;
import core.commands.WalletTypeCommandInterpreter;
import repositories.WalletRepository;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Currency;

import static common.SystemErrors.INCORRECT_WALLET_TYPE;

public class WalletTypeCommandInterpreterImpl implements WalletTypeCommandInterpreter {

    private static final String COMMAND_PACKAGE_NAME = "core.commands.";

    private final UserSessionManager sessionManager;
    private final WalletRepository walletRepository;

    public WalletTypeCommandInterpreterImpl(UserSessionManager sessionManager, WalletRepository walletRepository) {
        this.sessionManager = sessionManager;
        this.walletRepository = walletRepository;
    }

    @Override
    public Executable interpretCommand(Currency currency, String walletType) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String className = COMMAND_PACKAGE_NAME + walletType + "WalletCommand";

        Class<WalletTypeCommand> commandClass;
        try {
            commandClass = (Class<WalletTypeCommand>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(INCORRECT_WALLET_TYPE);
        }

        Constructor<WalletTypeCommand> constructor = commandClass
                .getDeclaredConstructor(UserSessionManager.class, WalletRepository.class, Currency.class);
        constructor.setAccessible(true);

        WalletTypeCommand walletTypeCommand = constructor.newInstance(sessionManager, walletRepository, currency);

        return walletTypeCommand;
    }
}