package android.network.sdk.listener;


import android.network.protocol.Message;

/**
 * @author Mr.Huang
 * @date 2017/8/23
 */
public interface OnReceiverMessage {

    void onMessage(Message message);

    void onStatus(int status);

}
