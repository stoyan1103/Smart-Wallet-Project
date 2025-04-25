package core.commands;

import java.lang.reflect.InvocationTargetException;
import java.util.Currency;

public interface WalletTypeCommandInterpreter {

    Executable interpretCommand(Currency currency, String walletType) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;
}