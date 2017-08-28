package android.network.invoke;

import android.network.binder.local.ILocalBinder;
import android.network.local.LocalServiceConnection;
import android.os.Handler;
import android.os.Looper;

/**
 * @author Mr.Huang
 * @date 2017/8/23
 */
public abstract class LocalBinderInvoke {

    private LocalServiceConnection connection;
    private Handler handler;

    public LocalBinderInvoke(LocalServiceConnection connection) {
        this.connection = connection;
        handler = new Handler(Looper.getMainLooper());
    }

    protected LocalServiceConnection getConnection(){
        return connection;
    }

    public void login(int uid, String token){
        loopInvokeLogin(uid, token);
    }


    public void sendText(int receiver, int type, String text){
        loopInvokeSendText(receiver, type, text);
    }

    public void logout(){
        loopInvokeLogout();
    }

    private void loopInvokeSendText(final int receiver, final int type, final String text) {
        if (!invokeSendText(connection.getBinder(), receiver, type, text)) {
            new LoopInvoke(handler) {
                @Override
                protected boolean invoke() {
                    return invokeSendText(connection.getBinder(), receiver, type, text);
                }
            }.start();
        }
    }

    protected void loopInvokeLogin(final int uid, final String token) {
        if (!invokeLogin(connection.getBinder(), uid, token)) {
            new LoopInvoke(handler) {
                @Override
                protected boolean invoke() {
                    return invokeLogin(connection.getBinder(), uid, token);
                }
            }.start();
        }
    }

    protected void loopInvokeLogout() {
        if (!invokeLogout(connection.getBinder())) {
            new LoopInvoke(handler) {
                @Override
                protected boolean invoke() {
                    return invokeLogout(connection.getBinder());
                }
            }.start();
        }
    }


    private static boolean invokeLogin(ILocalBinder binder, int uid, String token) {
        try {
            if (binder != null) {
                binder.login(uid, token);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean invokeLogout(ILocalBinder binder) {
        try {
            if (binder != null) {
                binder.logout();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean invokeSendText(ILocalBinder binder, int receiver, int type, String text) {
        try {
            if (binder != null) {
                binder.sendText(receiver, type, text);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
