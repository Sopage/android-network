package android.network.binder.local;

interface ILocalBinder {

    boolean login(int uid, String token);
    boolean logout();

}
