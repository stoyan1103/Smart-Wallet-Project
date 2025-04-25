package core;

import entities.user.User;

public class UserSessionManager implements SessionManager<User> {

    private User currentlyActiveUser;

    @Override
    public User getActiveSession() {
        return currentlyActiveUser;
    }

    @Override
    public void setActiveSession(User user) {
        this.currentlyActiveUser = user;
    }

    @Override
    public boolean hasActiveSession() {
        return currentlyActiveUser != null;
    }

    @Override
    public void terminateActiveSession() {
        currentlyActiveUser = null;
    }
}