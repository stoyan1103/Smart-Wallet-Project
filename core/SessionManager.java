package core;

public interface SessionManager<T> {

    T getActiveSession();

    void setActiveSession(T user);

    boolean hasActiveSession();

    void terminateActiveSession();
}
