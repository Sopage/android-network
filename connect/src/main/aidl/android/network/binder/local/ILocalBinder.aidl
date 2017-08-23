package android.network.binder.local;

import android.network.binder.local.ILocalCallback;

interface ILocalBinder {

    boolean register(ILocalCallback cb);

    boolean unregister(ILocalCallback cb);

    boolean login(int uid, String token);

    boolean logout();

    boolean sendText(int receiver, int type, String text);

}
