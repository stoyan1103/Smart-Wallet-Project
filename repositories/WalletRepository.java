package repositories;

import entities.wallet.Wallet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WalletRepository implements Repository<Wallet, UUID> {

    private static final Map<UUID, Wallet> WALLET_STORAGE = new HashMap<>();

    @Override
    public void save(UUID id, Wallet wallet) {
        WALLET_STORAGE.put(id, wallet);
    }

    @Override
    public Wallet getById(UUID id) {
        return WALLET_STORAGE.get(id);
    }

    @Override
    public List<Wallet> getAll() {
        return WALLET_STORAGE.values().stream().toList();
    }
}