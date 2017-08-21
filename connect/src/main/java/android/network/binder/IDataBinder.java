package android.network.binder;

/**
 * @author Mr.Huang
 * @date 2017/8/21
 */
public interface IDataBinder {

    void login(int uid, String token);

    void logout();
}
