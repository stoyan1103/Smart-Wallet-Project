package entities.wallet;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

import static common.SystemErrors.SAVINGS_PERIOD_NOT_CONCLUDED_YET;

public class SavingsWallet extends Wallet {

    private static final double INITIAL_BALANCE = 10.00;

    private LocalDateTime savingPeriodEnd;

    public SavingsWallet(UUID ownerId, String ownerUsername, Currency currency) {
        super(ownerId, ownerUsername, currency, INITIAL_BALANCE);
        this.savingPeriodEnd = LocalDateTime.now().plusMinutes(2);
    }

    @Override
    public void withdraw(double amount) {

        if (LocalDateTime.now().isBefore(savingPeriodEnd)) {
            long leftSeconds = Duration.between(LocalDateTime.now(), savingPeriodEnd).toSeconds();
            throw new IllegalStateException(SAVINGS_PERIOD_NOT_CONCLUDED_YET.formatted(leftSeconds));
        }

        super.withdraw(amount);
        resetSavingPeriodEnd();
    }

    private void resetSavingPeriodEnd() {
        this.savingPeriodEnd = LocalDateTime.now().plusMinutes(2);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(super.toString());

        long seconds = Duration.between(LocalDateTime.now(), savingPeriodEnd).toSeconds();

        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("Saving period ends within: %s seconds".formatted(Math.max(0, seconds)));

        return stringBuilder.toString();
    }
}