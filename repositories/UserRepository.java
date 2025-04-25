package repositories;

import entities.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserRepository implements Repository<User, UUID> {

    private static final Map<UUID, User> USER_STORAGE = new HashMap<>();

    @Override
    public void save(UUID uuid, User user) {
        USER_STORAGE.put(uuid, user);
    }

    @Override
    public User getById(UUID uuid) {
        return USER_STORAGE.get(uuid);
    }

    @Override
    public List<User> getAll() {
        return USER_STORAGE.values().stream().toList();
    }
}